package net.karmats.sverigeleden.shared.dto;

import java.io.Serializable;

public class LatLngDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private double lat;
    private double lng;

    public LatLngDto() {
    }

    public LatLngDto(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return "Lat: " + lat + ", Lng: " + lng;
    }
}
