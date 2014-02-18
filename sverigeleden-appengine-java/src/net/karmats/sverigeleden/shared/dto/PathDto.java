package net.karmats.sverigeleden.shared.dto;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Polyline;

public class PathDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private float distance;
    private List<LatLngDto> latLngDtos;
    private LineStyleDto lineStyle;
    private boolean approved;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public void setLatLngs(List<LatLngDto> latLngs) {
        this.latLngDtos = latLngs;
    }

    public List<LatLngDto> getLatLngs() {
        return latLngDtos;
    }

    public LineStyleDto getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(LineStyleDto lineStyle) {
        this.lineStyle = lineStyle;
    }


    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
    
    public Polyline toPolyLine() {
        LatLng[] latLngs = new LatLng[this.latLngDtos.size()];
        for (int i = 0; i < latLngDtos.size(); i++) {
            latLngs[i] = LatLng.newInstance(latLngDtos.get(i).getLat(), latLngDtos.get(i).getLng());
        }
        return new Polyline(latLngs, "#" + lineStyle.getColor().substring(2), lineStyle.getWidth(), 0.5);
    }

}
