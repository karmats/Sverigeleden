package net.karmats.sverigeleden.shared.dto;

import java.io.Serializable;

public class LineStyleDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer width;

    private String color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
