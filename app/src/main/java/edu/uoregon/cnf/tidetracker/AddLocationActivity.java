package edu.uoregon.cnf.tidetracker;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddLocationActivity extends AppCompatActivity implements View.OnClickListener{

    private Button addLocationButton;
    private TextView errorTextView;
    private EditText locationNameEditText;
    private EditText locationCodeEditText;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private TideTrackerDB db;
    private TideTrackerDBHelper dbHelper;
    private Location location;
    private Spinner typeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        addLocationButton = (Button)findViewById(R.id.addLocationButton);
        errorTextView = (TextView)findViewById(R.id.errorTextView);
        locationNameEditText = (EditText)findViewById(R.id.locationNameEditText);
        locationCodeEditText = (EditText)findViewById(R.id.locationCodeEditText);
        longitudeEditText = (EditText)findViewById(R.id.longitudeEditText);
        latitudeEditText = (EditText)findViewById(R.id.latitudeEditText);

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.predictiontypes_array, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        typeSpinner.setAdapter(adapter);

        addLocationButton.setOnClickListener(this);

        location = new Location();

        db = new TideTrackerDB(this);
    }

    public void addLocation()
    {
        if(locationCodeEditText.getText().toString().length() > 0)
        {
            String locationCode = locationCodeEditText.getText().toString();
            Location location = db.getLocationByCode(locationCode);
            if(location != null)
            {
                errorTextView.setText("Location already exists in the database");
            }
            else
            {
                if(locationNameEditText.getText().toString().length() == 0 ||
                        longitudeEditText.getText().toString().length() == 0 ||
                        latitudeEditText.getText().toString().length() == 0)
                {
                    errorTextView.setText("Please enter a value for name, code, longitude, and latitude.");
                }
                else
                {
                    location.setName(locationNameEditText.getText().toString());
                    location.setLocationCode(locationCodeEditText.getText().toString());
                    location.setLatitude(latitudeEditText.getText().toString());
                    location.setLatitude(longitudeEditText.getText().toString());
                    location.setPredictionType(typeSpinner.getSelectedItem().toString());

                    if(db.insertLocation(location))
                    {
                        Toast toast = Toast.makeText(this, "Location inserted", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    {
                        Toast toast = Toast.makeText(this, "Location insert failed", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            }
        }
        else
        {
            Toast toast = Toast.makeText(this, "You must enter a location code", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.addLocationButton)
        {
            addLocation();
        }
    }
}
