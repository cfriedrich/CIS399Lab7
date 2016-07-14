//package edu.uoregon.cnf.tidetracker;
//
//import android.os.AsyncTask;
//import android.widget.Toast;
//
//import org.xmlpull.v1.XmlPullParserException;
//
//import java.io.IOException;
//import java.net.HttpRetryException;
//import java.net.Proxy;
//
//import org.ksoap2.SoapEnvelope;
//import org.ksoap2.serialization.SoapObject;
//import org.ksoap2.serialization.SoapSerializationEnvelope;
//import org.ksoap2.transport.HttpTransportSE;
//
//import java.io.IOException;
//import java.util.List;
//
//public class TideTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            // Create a SOAP request object and put it in an envelope
//            final String REQUEST_URL = "http://opendap.co-ops.nos.noaa.gov/axis/webservices/waterlevelverifiedmonthly/wsdl";
//            final String SOAP_OP = "getWLVerifiedMonthlyAndMetadata";
//            SoapObject request = new SoapObject(REQUEST_URL, SOAP_OP);
//            request.addProperty("stationId", params[0]);
//            request.addProperty("beginDate", params[1]);
//            request.addProperty("endDate", params[2]);
//
//            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
//            envelope.dotNet = true;
//            envelope.implicitTypes = true;
//            envelope.setOutputSoapObject(request);
//
//            // Send the request (call the SOAP method)
//
//            HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, REQUEST_URL, 60000);
//            ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
//            ht.debug = true;
//
//            try {
//                ht.call(REQUEST_URL + SOAP_OP, envelope); // first parameter is soapAction
//            } catch (HttpRetryException e) {
//
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (XmlPullParserException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//
//            // Get the response from the SOAP service
//			/*
//			// This works, but doesn't give us the raw XML
//			SoapObject response = null;
//			try {
//				response = (SoapObject)envelope.getResponse();
//			} catch (SoapFault e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			weatherXml = response.getPropertyAsString("GetCityForecastByZIPResult");
//			*/
//
//            // This gives us the raw XML
//            String predictionsXml = ht.responseDump;
//            return predictionsXml;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            if (result.contains("resource cannot be found")) {
//                Toast.makeText(SelectionActivity.this, "Web Service:No data available", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                dal.loadDbFromWebService(result, locationSelection);
//            }
//
//            cursor = dal.getForcastFromDb(locationSelection);    // reads it back from the db
//            if (cursor.getCount() == 0) {
//                Toast.makeText(MainActivity.this, "Database:No data available", Toast.LENGTH_SHORT).show();
//            } else {
//                adapter.changeCursor(cursor);
//            }
//        }
//
//    }
//}
