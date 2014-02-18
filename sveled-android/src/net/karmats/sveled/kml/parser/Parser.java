package net.karmats.sveled.kml.parser;

import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.google.android.maps.GeoPoint;

public interface Parser {

    List<GeoPoint> parse() throws XmlPullParserException, IOException;

}
