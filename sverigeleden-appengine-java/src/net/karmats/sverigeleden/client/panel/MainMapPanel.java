package net.karmats.sverigeleden.client.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.karmats.sverigeleden.client.dialog.WaitDialog;
import net.karmats.sverigeleden.client.service.MapKmlService;
import net.karmats.sverigeleden.client.service.MapKmlServiceAsync;
import net.karmats.sverigeleden.client.service.PathService;
import net.karmats.sverigeleden.client.service.PathServiceAsync;
import net.karmats.sverigeleden.shared.dto.LatLngDto;
import net.karmats.sverigeleden.shared.dto.LineStyleDto;
import net.karmats.sverigeleden.shared.dto.PathDto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.GeoXmlLoadCallback;
import com.google.gwt.maps.client.overlay.GeoXmlOverlay;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainMapPanel extends HorizontalPanel {

    private static final String KML_LOCATION = "http://dl.dropbox.com/u/14430311/";

    private Map<String, GeoXmlOverlay> geoXmlOverlays = new HashMap<String, GeoXmlOverlay>();

    /**
     * Create a remote service proxy to talk to the server-side services.
     */
    private final PathServiceAsync pathService = GWT.create(PathService.class);
    private final MapKmlServiceAsync mapKmlService = GWT.create(MapKmlService.class);

    public MainMapPanel() {
        super();
        Maps.loadMapsApi("", "2", false, new Runnable() {
            public void run() {
                buildUi();
            }
        });

    }

    private void buildUi() {
        // Build the map
        setSize("100%", "100%");
        LatLng chicago = LatLng.newInstance(57.700092, 11.951909);
        final MapWidget map = new MapWidget(chicago, 7);
        map.setSize("700px", "700px");
        map.setCurrentMapType(MapType.getSatelliteMap());
        map.addControl(new MapTypeControl());
        // Add some controls for the zoom level
        map.addControl(new LargeMapControl());
        map.setScrollWheelZoomEnabled(true);

        // Check box panel for download kml file
        final FormPanel downloadPanel = new FormPanel("_self");
        downloadPanel.setMethod(FormPanel.METHOD_POST);
        downloadPanel.setAction("/sverigeleden/download");
        final VerticalPanel pathsPanel = new VerticalPanel();
        downloadPanel.add(pathsPanel);
        pathService.getAllPaths(new AsyncCallback<List<PathDto>>() {

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Failed to get all paths, " + caught);
            }

            @Override
            public void onSuccess(List<PathDto> result) {
                final Button generateKmlButton = new Button("Generate KML");
                for (final PathDto pathDto : result) {
                    final String pathName = pathDto.getName();
                    final CheckBox cb = new CheckBox(pathName);
                    loadGeoXmlOverlay(pathName, map);
                    cb.setValue(true);
                    cb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

                        @Override
                        public void onValueChange(ValueChangeEvent<Boolean> event) {
                            if (event.getValue()) {
                                loadGeoXmlOverlay(pathName, map);
                            } else {
                                map.removeOverlay(geoXmlOverlays.get(pathName));
                            }
                            // Check if all check buttons are unchecked, if that's the case disable generateKmlButton
                            boolean enable = false;
                            for (int i = 0; i < pathsPanel.getWidgetCount(); i++) {
                                Widget w = pathsPanel.getWidget(i);
                                if (w instanceof CheckBox) {
                                    if (((CheckBox) w).getValue()) {
                                        enable = true;
                                        break;
                                    }
                                }
                            }
                            generateKmlButton.setEnabled(enable);

                        }
                    });
                    pathsPanel.add(cb);
                }
                generateKmlButton.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        List<String> selectedPaths = new ArrayList<String>();
                        // Get all selected paths from the checkbox list
                        for (int i = 0; i < pathsPanel.getWidgetCount(); i++) {
                            Widget w = pathsPanel.getWidget(i);
                            if (w instanceof CheckBox) {
                                CheckBox cb = (CheckBox) w;
                                if (cb.getValue()) {
                                    selectedPaths.add(cb.getText());
                                }
                            }
                        }
                        mapKmlService.generateKml(selectedPaths, new AsyncCallback<String>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                GWT.log("Failed to generate kml for paths", caught);
                            }

                            @Override
                            public void onSuccess(String result) {
                                pathsPanel.add(new Hidden("kmlData", result));
                                downloadPanel.submit();
                            }
                        });
                    }
                });
                pathsPanel.add(generateKmlButton);
            }
        });

        // Upload panel
        final FormPanel uploadPanel = new FormPanel();
        uploadPanel.setAction("/sverigeleden/upload");
        uploadPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        uploadPanel.setMethod(FormPanel.METHOD_POST);
        final VerticalPanel vp = new VerticalPanel();
        final VerticalPanel radioButtonPanel = new VerticalPanel();
        final WaitDialog waitDialog = new WaitDialog();
        pathService.getAllLineStyles(new AsyncCallback<List<LineStyleDto>>() {

            @Override
            public void onSuccess(List<LineStyleDto> result) {
                for (LineStyleDto ls : result) {
                    RadioButton rb = new RadioButton("color", ls.getColor());
                    radioButtonPanel.add(rb);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Failed to get linestyles", caught);
            }
        });

        final FileUpload fu = new FileUpload();
        fu.setName("Filen");
        Button submitButton = new Button("Upload");
        submitButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uploadPanel.submit();
            }
        });

        uploadPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
                mapKmlService.parseRawKml(event.getResults(), new AsyncCallback<List<PathDto>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        GWT.log("Failed to parse kml", caught);
                    }

                    @Override
                    public void onSuccess(List<PathDto> result) {
                        waitDialog.hide();
                        GWT.log("Successfully parsed kml, path dtos: " + result.size());
                        for (PathDto pathDto : result) {
                            LatLng[] latLngs = new LatLng[pathDto.getLatLngs().size()];
                            for (int i = 0; i < pathDto.getLatLngs().size(); i++) {
                                LatLngDto latLngDto = pathDto.getLatLngs().get(i);
                                latLngs[i] = LatLng.newInstance(latLngDto.getLat(), latLngDto.getLng());
                            }
                            Polyline polOverlay = new Polyline(latLngs, "#FFF000", 5, 0.7);
                            map.addOverlay(polOverlay);
                            map.panTo(polOverlay.getBounds().getCenter());
                        }
                    }
                });

            }
        });
        uploadPanel.addSubmitHandler(new SubmitHandler() {

            @Override
            public void onSubmit(SubmitEvent event) {
                String fileName = fu.getFilename();
                if (fileName != null && (fileName.endsWith(".gpx") || fu.getFilename().endsWith(".kml"))) {
                    waitDialog.center();
                    waitDialog.show();
                } else {
                    GWT.log(fu.getFilename() + " is of wrong format");
                    event.cancel();
                }
            }
        });

        vp.add(new Label("Namn"));
        vp.add(new TextBox());
        vp.add(radioButtonPanel);
        vp.add(fu);
        vp.add(submitButton);
        uploadPanel.add(vp);

        // Create a tabbed panel for the download and upload panels
        TabPanel tabPanel = new TabPanel();
        tabPanel.add(downloadPanel, "Download");
        tabPanel.add(uploadPanel, "Upload");
        tabPanel.selectTab(0);

        add(tabPanel);
        add(map);

    }

    protected void loadGeoXmlOverlay(final String pathName, final MapWidget map) {
        GeoXmlOverlay geoOverlay = geoXmlOverlays.get(pathName);
        if (geoOverlay != null) {
            map.addOverlay(geoOverlay);
        } else {
            GeoXmlOverlay.load(URL.encode(KML_LOCATION + pathName + ".kml"), new GeoXmlLoadCallback() {

                @Override
                public void onSuccess(String url, GeoXmlOverlay overlay) {
                    if (!geoXmlOverlays.containsValue(overlay)) {
                        geoXmlOverlays.put(pathName, overlay);
                        map.addOverlay(overlay);
                    }
                }

                @Override
                public void onFailure(String url, Throwable caught) {
                    GWT.log("Failed to load overlay from " + url, caught);
                }
            });

        }

    }

}
