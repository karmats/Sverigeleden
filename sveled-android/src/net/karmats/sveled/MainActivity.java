package net.karmats.sveled;

import java.io.IOException;

import net.karmats.sveled.kml.model.CyclePathOverlay;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class MainActivity extends MapActivity {
    
    private CyclePathOverlay overlay;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setSatellite(true);
        mapView.getController().animateTo(new GeoPoint((int) (64.5908644 * 1E6), (int) (18.6905064 * 1E6)));
        mapView.getController().setZoom(15);

        Button b = (Button) findViewById(R.id.toogleButton);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapView.getOverlays().isEmpty()) {
                    try {
                        if (overlay == null) {
                            overlay = new CyclePathOverlay(getAssets().open("karta.gpx"));
                        }
                        mapView.getOverlays().add(overlay);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    mapView.getOverlays().clear();
                }
                mapView.invalidate();
            }
        });
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}