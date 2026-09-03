package se.sofievd.backend.weather.smhi.model;

import java.util.List;

public class SmhiPolygon {
    private String type;
    private List<List<List<Double>>> coordinates;

    public SmhiPolygon() {
    }

    public List<List<List<Double>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
