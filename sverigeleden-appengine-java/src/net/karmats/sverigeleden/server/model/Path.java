package net.karmats.sverigeleden.server.model;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Path {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String name;

    @Persistent
    private Text latLngs;

    @Persistent
    private float distance;

    @Persistent
    @Element(dependent = "true")
    private LineStyle style;

    @Persistent
    private boolean approved;

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public LineStyle getStyle() {
        return style;
    }

    public void setStyle(LineStyle style) {
        this.style = style;
    }

    public Text getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(Text latLngs) {
        this.latLngs = latLngs;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

}
