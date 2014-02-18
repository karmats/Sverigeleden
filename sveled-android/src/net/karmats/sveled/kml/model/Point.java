package net.karmats.sveled.kml.model;

public class Point {

    private final int longitude;
    private final int latitude;

    public Point(int latitude, int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getLatitude() {
        return latitude;
    }
    
    @Override
    public String toString() {
        return this.latitude + ", " + this.longitude;
    }

}
