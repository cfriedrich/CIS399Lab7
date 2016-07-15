package edu.uoregon.cnf.tidetracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpRetryException;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private SimpleDateFormat niceDateOutFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
    private SimpleDateFormat day1OutFormat = new SimpleDateFormat("MMMM dd -");
    private SimpleDateFormat day2OutFormat = new SimpleDateFormat(" dd, yyyy");
    private SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    private SimpleDateFormat soapFormat = new SimpleDateFormat("yyyyMMdd");


    private Spinner locationSpinner;
    private Button tideInfoButton;
    private DatePicker readingDatePicker;
    private TideTrackerDB db;
    private ArrayList<Location> locations;
    private String selectedLocation;
    private Cursor cursor;

    private DAL dal = new DAL(this);

    String formattedDate = "";
    String formattedDate2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        db = new TideTrackerDB(this);

        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);

        locations = new ArrayList<Location>();

        // Get the predictions
        locations.addAll(db.getLocations());

        String[] locationNames = new String[locations.size()];
        for(int i=0; i < locations.size(); i++)
        {
            locationNames[i] = locations.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, locationNames);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        locationSpinner.setAdapter(adapter);

        tideInfoButton = (Button) findViewById(R.id.tideInfoButton);
        tideInfoButton.setOnClickListener(this);

        readingDatePicker = (DatePicker) findViewById(R.id.readingDatePicker);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tideInfoButton)
        {
            if(locationSpinner.getSelectedItemPosition() == 0)
            {
                Toast toast = Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                String selectedLocation = locationSpinner.getSelectedItem().toString();

                if(readingDatePicker.getYear() != 2016)
                {
                    Toast toast = Toast.makeText(this, "Please select a date within 2016", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    int day = readingDatePicker.getDayOfMonth();
                    int month = readingDatePicker.getMonth();
                    int year =  readingDatePicker.getYear();

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);

                    Date selectedDate =  calendar.getTime();

                    String soapFormattedDay1 = soapFormat.format(selectedDate);
                    formattedDate = shortDateFormat.format(selectedDate);
                    String niceFormattedDate = niceDateOutFormat.format(selectedDate);

                    formattedDate2 = formattedDate;
                    String soapFormattedDay2 = soapFormat.format(selectedDate);
                    if(month != 12 && day != 31)
                    {
                        calendar.add(Calendar.DATE, 1);
                        Date dayAfter = calendar.getTime();
                        soapFormattedDay2 = soapFormat.format(dayAfter);
                        formattedDate2 = shortDateFormat.format(dayAfter);
                        niceFormattedDate = day1OutFormat.format(selectedDate) + day2OutFormat.format(dayAfter);
                    }

                    Intent intent = new Intent(this, ViewTidesActivity.class);

                    if(db.getPredictions(selectedLocation, formattedDate, formattedDate2).size() == 0)
                    {
                        Location location = db.getLocationByName(selectedLocation);
                        TideTask tideTask = new TideTask();
                        tideTask.execute(location.getLocationCode(), soapFormattedDay1, soapFormattedDay2);		// get's the forecast and puts it in the db
                    }
                    intent.putExtra("location", selectedLocation);
                    intent.putExtra("date", formattedDate);
                    intent.putExtra("date2", formattedDate2);
                    intent.putExtra("niceDate", niceFormattedDate);

                    this.startActivity(intent);

                }
            }
        }
    }

    private final SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);

        return envelope;
    }

    private static final String MAIN_REQUEST_URL = "http://opendap.co-ops.nos.noaa.gov/axis/services/highlowtidepred";

    private final HttpTransportSE getHttpTransportSE() {
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,MAIN_REQUEST_URL,60000);
        ht.debug = true;
        ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
        return ht;
    }

    public class TideTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            // Create a SOAP request object and put it in an envelope
            final String NAMESPACE = "http://opendap.co-ops.nos.noaa.gov/axis/webservices/highlowtidepred/wsdl";
            final String METHOD_NAME = "getHighLowTidePredictions";
            final String URL = "http://opendap.co-ops.nos.noaa.gov/axis/services/highlowtidepred";
            final String SOAP_ACTION = URL + "/" + METHOD_NAME;
            SoapObject request = new SoapObject(URL, METHOD_NAME);
//            SoapObject request = new SoapObject(URL, SOAP_ACTION);
            request.addProperty("stationId", params[0]);
            request.addProperty("beginDate", params[1]);
            request.addProperty("endDate", params[2]);
            request.addProperty("datum", "MLLW");
            request.addProperty("unit", "0");
            request.addProperty("timeZone", "0");
//            request.addProperty("format", "xml");


            SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
            envelope.dotNet = true;
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            // Send the request (call the SOAP method)
            HttpTransportSE ht = getHttpTransportSE();
//            ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
//            ht.debug = true;

            try {
                ht.call(SOAP_ACTION, envelope); // first parameter is soapAction
            } catch (HttpRetryException e) {

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            // Get the response from the SOAP service
			/*
			// This works, but doesn't give us the raw XML
			SoapObject response = null;
			try {
				response = (SoapObject)envelope.getResponse();
			} catch (SoapFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			weatherXml = response.getPropertyAsString("GetCityForecastByZIPResult");
			*/

            // This gives us the raw XML
            String predictionsXml = ht.responseDump;
            return predictionsXml;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.contains("resource cannot be found")) {
                Toast.makeText(SelectionActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
            }
            else {
                dal.fillDBWithLocationPredictions(result, selectedLocation);
            }

            ArrayList<Prediction> predictions = dal.getPredictionsFromDb(db, selectedLocation, formattedDate, formattedDate2);    // reads it back from the db
            if (cursor.getCount() == 0) {
                Toast.makeText(SelectionActivity.this, "Database:No data available", Toast.LENGTH_SHORT).show();
            } else {
                //adapter.changeCursor(cursor);
            }
        }

    }
}

