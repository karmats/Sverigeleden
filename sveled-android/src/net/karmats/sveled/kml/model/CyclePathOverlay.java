package net.karmats.sveled.kml.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import net.karmats.sveled.kml.parser.PlacemarkPullParser;

import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.location.Location;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class CyclePathOverlay extends Overlay {

    private static final int ZOOM_LEVEL_ALL_POINTS = 15;

    private List<GeoPoint> points;

    public CyclePathOverlay(InputStream mapSource) {
        Log.i(getClass().getName(), "Parsing karta.gpx");
        PlacemarkPullParser ppp = new PlacemarkPullParser(mapSource);
        try {
            long start = System.currentTimeMillis();
            points = ppp.parse();
            Log.i(getClass().getName(), "Parsing took " + (System.currentTimeMillis() - start) + " ms");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(getClass().getName(), "Done parsing karta.xml, found " + points.size() + " points");
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);

        if (shadow == false) {
            Projection projection = mapView.getProjection();

            List<GeoPoint> pointsToDraw = getPointsToDraw(projection.fromPixels(0, 0), projection.fromPixels(mapView.getWidth(), mapView.getHeight()),
                                                          mapView.getZoomLevel());
            Paint mPaint = new Paint();
            mPaint.setStyle(Style.STROKE);
            mPaint.setColor(Color.RED);
            mPaint.setAntiAlias(true);
            for (int i = 0; i < pointsToDraw.size() - 1; i++) {
                GeoPoint start = pointsToDraw.get(i);
                GeoPoint end = pointsToDraw.get(i + 1);
                float[] dist = new float[1];
                Location.distanceBetween(start.getLatitudeE6()/1E6, start.getLongitudeE6()/1E6, end.getLatitudeE6()/1E6, end.getLongitudeE6()/1E6, dist);
                if (dist[0] > 1000) {
                    Log.i(getClass().getName(), "Ignoring point distance " + dist[0] + "is too long");
                    continue;
                }
                android.graphics.Point point = new android.graphics.Point();
                projection.toPixels(start, point);
                android.graphics.Point point2 = new android.graphics.Point();
                projection.toPixels(end, point2);
                canvas.drawLine(point.x, point.y, point2.x, point2.y, mPaint);
            }
        }
    }

    private List<GeoPoint> getPointsToDraw(GeoPoint topLeft, GeoPoint bottomRight, int zoomLevel) {

        List<GeoPoint> pointsToDraw = new LinkedList<GeoPoint>();
        int skipStep = ZOOM_LEVEL_ALL_POINTS - zoomLevel <= 0 ? 1 : (ZOOM_LEVEL_ALL_POINTS - zoomLevel) * 2;
        for (int i = 0; i < points.size(); i = i + skipStep) {
            GeoPoint p = points.get(i);
            int lat = p.getLatitudeE6();
            int lon = p.getLongitudeE6();
            if (lon >= topLeft.getLongitudeE6() && lon <= bottomRight.getLongitudeE6() && lat <= topLeft.getLatitudeE6() && lat >= bottomRight.getLongitudeE6()) {
                pointsToDraw.add(p);
            }
        }
        return pointsToDraw;
    }

}
