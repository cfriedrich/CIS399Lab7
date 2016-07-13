package edu.uoregon.cnf.tidetracker;

import android.content.Context;
import android.content.res.AssetManager;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class FileIO {

    private ArrayList<DataItem> dataItems = new ArrayList<DataItem>();

    private Context context = null;

    public FileIO (Context context) { this.context = context;}

    // For reading all of the files
    public ArrayList<DataItem> readAllFiles(String[] files)
    {
        for(String fileName : files) {
            ParsedData fileData = readFile(fileName);
            dataItems.addAll(fileData.getAllItems());
        }
        return dataItems;
    }

    public ParsedData readFile(String FILENAME) {
        try {
            // Get the XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // Set content handler
            DataParser dataParser = new DataParser();
            xmlreader.setContentHandler(dataParser);

            AssetManager assetManager = context.getAssets();

            // Read the file from internal storage
            InputStream inStream = assetManager.open(FILENAME);

            // Parse the data
            InputSource inSource = new InputSource(inStream);
            xmlreader.parse(inSource);

            // Set the feed in the activity
            return dataParser.getFeed();
        }
        catch (Exception e) {
            return null;
        }
    }
}
