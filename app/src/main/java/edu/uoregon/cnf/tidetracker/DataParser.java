package edu.uoregon.cnf.tidetracker;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.SimpleDateFormat;

public class DataParser extends DefaultHandler {
    private ParsedData dataCollection;
    private DataItem dataItem;
    private SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.0");
    private SimpleDateFormat dayFormat = new SimpleDateFormat("DDD");
    private SimpleDateFormat shortDateOutFormat = new SimpleDateFormat("yyyy/MM/dd");

    private boolean isTimestamp = false;
    private boolean isWaterlevel = false;
//    private boolean isDay = false;
//    private boolean isTime = false;
//    private boolean isFeet = false;
//    private boolean isCentimeters = false;
//    private boolean isHighLow = false;

    static String location;

    public ParsedData getFeed() {
        return dataCollection;
    }

    @Override
    public void startDocument() throws SAXException {
        dataCollection = new ParsedData();
        dataItem = new DataItem();
    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        if (qName.equals("item")) {
            dataItem = new DataItem();
            return;
        }
        if (qName.equals("timeStamp")) {
            isTimestamp = true;
            return;
        }
        else if (qName.equals("WL")) {
            isWaterlevel = true;
            return;
        }
//        else if (qName.equals("day")) {
//            isDay = true;
//            return;
//        }
//        else if (qName.equals("time")) {
//            isTime = true;
//            return;
//        }
//        else if (qName.equals("predictions_in_ft")) {
//            isFeet = true;
//            return;
//        }
//        else if (qName.equals("predictions_in_cm")) {
//            isCentimeters = true;
//            return;
//        }
//        else if (qName.equals("highlow")){
//            isHighLow = true;
//            return;
//        }
    }

    @Override
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException
    {
        if (qName.equals("item")) {
            dataItem.setLocation(location);
            dataCollection.addItem(dataItem);
            return;
        }
//        if (qName.equals("stationname")) {
//            return;
//        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
        String s = new String(ch, start, length);
        if (isTimestamp) {
            dataItem.setDateString(s);
            isTimestamp = false;
        }
        if (isWaterlevel){
            dataItem.setCentimeters(s);
//            location = s.trim().toLowerCase().substring(0, 3);
            isWaterlevel = false;
        }
//        if (isDay) {
//            String dayName = null;
//            switch(s)
//            {
//                case "Sun":
//                    dayName = "Sunday";
//                    break;
//                case "Mon":
//                    dayName = "Monday";
//                    break;
//                case "Tue":
//                    dayName = "Tuesday";
//                    break;
//                case "Wed":
//                    dayName = "Wednesday";
//                    break;
//                case "Thu":
//                    dayName = "Thursday";
//                    break;
//                case "Fri":
//                    dayName = "Friday";
//                    break;
//                case "Sat":
//                    dayName = "Saturday";
//                    break;
//            }
//            dataItem.setDay(dayName);
//            isDay = false;
//        }
//        else if (isTime) {
//            dataItem.setTime(s);
//            isTime = false;
//        }
//        else if (isFeet) {
//            dataItem.setFeet(s);
//            isFeet = false;
//        }
//        else if (isCentimeters) {
//            dataItem.setCentimeters(s);
//            isCentimeters = false;
//        }
//        else if (isHighLow){
//            String highLow = null;
//            char c = s.charAt(0);
//            if(c == 'H')
//                highLow = "High";
//            else
//                highLow = "Low";
//            dataItem.setHighlow(highLow);
//            isHighLow = false;
  //      }
    }
}
