package edu.uoregon.cnf.tidetracker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

// The new loading activity I created so you weren't staring at a blank screen...
public class LoadingActivity extends AppCompatActivity {

    private FileIO fileIO;
    private ArrayList<DataItem> dataCollection;
    private TextView loadingMessageTextView;
    private Spinner progressSpinner;
    private ProgressBar spinnerProgressBar;
    private TideTrackerDB db;

    private final String FILE1NAME = "astoria_annual.xml";
    private final String FILE2NAME = "florence_annual.xml";
    private final String FILE3NAME = "goldbeach_annual.xml";
    private final String FILE4NAME = "southbeach_annual.xml";

    private Context context;

    private String[] files = {
            FILE1NAME,
            FILE2NAME,
            FILE3NAME,
            FILE4NAME
    };

    private String[] locations = {
            FILE1NAME.substring(0, 3),
            FILE2NAME.substring(0, 3),
            FILE3NAME.substring(0, 3),
            FILE4NAME.substring(0, 3)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        loadingMessageTextView = (TextView)findViewById(R.id.loadingProgressTextView);
        loadingMessageTextView.setText("Reading Files...");

        spinnerProgressBar=(ProgressBar)findViewById(R.id.progressBar);

        // Initialize Database
        db = new TideTrackerDB(this);

        context = getApplicationContext();

        // Initialize FileIO
        fileIO = new FileIO(context);

        // Read the files
        new ReadFiles().execute();
    }

    // Load Selection view after we're done setting up the data
    private void loadSelectionActivity()
    {
        Intent intent = new Intent(getApplicationContext(), SelectionActivity.class);
        startActivity(intent);
    }

    // Read the file collection
    class ReadFiles extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            dataCollection = fileIO.readAllFiles(files);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Update the display
            loadingMessageTextView.setText("Populating Database...");
            new PopulateDatabase().execute();
        }
    }

        class PopulateDatabase extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                // Put the data in the database
                db.fillData(db, locations, dataCollection);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // And we're out of here...
                loadSelectionActivity();
            }
        }

}
