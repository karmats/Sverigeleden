package net.karmats.sveled.kml.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.google.android.maps.GeoPoint;

public class PlacemarkPullParser extends BaseParser {

    private static final String WPT = "rtept";

    public PlacemarkPullParser(InputStream source) {
        super(source);
    }

    @Override
    public List<GeoPoint> parse() throws XmlPullParserException, IOException {
        List<GeoPoint> points = null;
        XmlPullParser parser = Xml.newPullParser();

        // auto-detect the encoding from the stream
        parser.setInput(this.getInputStream(), "UTF-8");
        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
            case XmlPullParser.START_DOCUMENT:
                points = new LinkedList<GeoPoint>();
                break;
            case XmlPullParser.START_TAG:
                name = parser.getName();
                if (name.equalsIgnoreCase(WPT)) {
                    Float lat = Float.parseFloat(parser.getAttributeValue(null, "lat"));
                    Float lon = Float.parseFloat(parser.getAttributeValue(null, "lon"));
                    points.add(new GeoPoint((int)(lat*1E6), (int)(lon*1E6)));
                }
                break;
            case XmlPullParser.END_DOCUMENT:
                return points;
            }
            eventType = parser.next();
        }

        return points;
    }
}
