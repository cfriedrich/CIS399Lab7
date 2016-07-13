package edu.uoregon.cnf.tidetracker;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewTidesActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Prediction> datePredictions;
    private TideTrackerDB db;
    private ListView tidesListView;
    private String date;
    private String date2;
    private String fullDate;
    private String location;
    private int locationID;
    private TextView headingTextView;
    private TextView fullDateTextView;
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tides);
        Intent intent = getIntent();

        date = intent.getStringExtra("date");
        date2 = intent.getStringExtra("date2");
        fullDate = intent.getStringExtra("niceDate");
        location = intent.getStringExtra("location");
        String locationShort = location.toLowerCase().substring(0, 3);

        returnButton = (Button) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(this);

        db = new TideTrackerDB(this);

        locationID = db.getLocationID(locationShort);

        tidesListView = (ListView) findViewById(R.id.tidesListView);
        headingTextView = (TextView) findViewById(R.id.headingTextView);
        fullDateTextView = (TextView) findViewById(R.id.fullDateTextView);

        datePredictions = db.getPredictions(String.valueOf(locationID), date, date2);

        headingTextView.setText("Readings for " + location + ", Oregon");
        fullDateTextView.setText(fullDate);
        displayTideData();
    }

    private void displayTideData()
    {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        for (Prediction prediction : datePredictions) {
            HashMap<String, String> map = new HashMap<String, String>();
                map.put("date", prediction.getDate());
                map.put("time", prediction.getTime());
                map.put("highlow", prediction.getHighlow());
                map.put("feet", prediction.getFeet());
                map.put("cm", prediction.getCentimeters());
            data.add(map);
        }

        // Create the resource, from, and to variables
        int resource = R.layout.listview_tides;
        String[] from = {"date", "time", "highlow", "feet", "cm"};
        int[] to = {R.id.dateTextView, R.id.timeTextView, R.id.highLowTextView, R.id.feetTextView, R.id.centimetersTextView};

        // Create and set the adapter
        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        tidesListView.setAdapter(adapter);
    }

    // Allow the Up button to take the user back to Select screen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond Up button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {

                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.returnButton)
        {
            Intent intent = new Intent(this, SelectionActivity.class);
            intent.putExtra("locationID", String.valueOf(locationID));
            intent.putExtra("returning", "true");
            this.startActivity(intent);
        }
    }
}
