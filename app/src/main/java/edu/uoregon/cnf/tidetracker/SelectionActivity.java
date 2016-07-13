package edu.uoregon.cnf.tidetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.os.AsyncTask;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

public class SelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private SimpleDateFormat niceDateOutFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
    private SimpleDateFormat day1OutFormat = new SimpleDateFormat("MMMM dd -");
    private SimpleDateFormat day2OutFormat = new SimpleDateFormat(" dd, yyyy");
    private SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    private Spinner locationSpinner;
    private Button tideInfoButton;
    private DatePicker readingDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.locations_array, R.layout.spinner_item);

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
                String location = locationSpinner.getSelectedItem().toString();

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

                    String formattedDate = shortDateFormat.format(selectedDate);
                    String niceFormattedDate = niceDateOutFormat.format(selectedDate);

                    String formattedDate2 = formattedDate;
                    if(month != 12 && day != 31)
                    {
                        calendar.add(Calendar.DATE, 1);
                        Date dayAfter = calendar.getTime();
                        formattedDate2 = shortDateFormat.format(dayAfter);
                        niceFormattedDate = day1OutFormat.format(selectedDate) + day2OutFormat.format(dayAfter);
                    }

                    Intent intent = new Intent(this, ViewTidesActivity.class);

                    intent.putExtra("location", location);
                    intent.putExtra("date", formattedDate);
                    intent.putExtra("date2", formattedDate2);
                    intent.putExtra("niceDate", niceFormattedDate);

                    this.startActivity(intent);

                }
            }
        }
    }
}
