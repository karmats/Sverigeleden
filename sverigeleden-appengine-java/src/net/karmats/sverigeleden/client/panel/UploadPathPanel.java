package net.karmats.sverigeleden.client.panel;

import java.util.List;

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
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UploadPathPanel extends FormPanel {

    private MapWidget map;

    private final PathServiceAsync pathService = GWT.create(PathService.class);
    private final MapKmlServiceAsync mapKmlService = GWT.create(MapKmlService.class);

    public UploadPathPanel(MapWidget map) {
        super();

        this.map = map;
        setAction("/sverigeleden/upload");
        setEncoding(FormPanel.ENCODING_MULTIPART);
        setMethod(FormPanel.METHOD_POST);

        buildUi();
    }

    private void buildUi() {
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
                submit();
            }
        });

        addSubmitCompleteHandler(new SubmitCompleteHandler() {

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
        addSubmitHandler(new SubmitHandler() {

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

        add(vp);
    }
}
