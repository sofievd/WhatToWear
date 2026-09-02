package se.sofievd.backend.weather.smhi;

import jakarta.annotation.PostConstruct;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;
import se.sofievd.backend.weather.smhi.model.SmhiPolygon;

import java.util.List;

@Service
public class SmhiAreaService {

    private final SmhiClient smhiClient;
    private final GeometryFactory geometryFactory =
            new GeometryFactory();

    private Polygon polygon;

    public SmhiAreaService(SmhiClient smhiClient) {
        this.smhiClient = smhiClient;
    }

    @PostConstruct
    public void loadArea() {

        SmhiPolygon smhiPolygon =
                smhiClient.getForecastArea();

        this.polygon = createPolygon(smhiPolygon);
    }

    public boolean isInsideArea(double lon, double lat) {

        Point point = geometryFactory.createPoint(
                new Coordinate(lon, lat)
        );

        return polygon.contains(point);
    }

    private Polygon createPolygon(SmhiPolygon smhiPolygon){
        List<List<Double>> ring = smhiPolygon.getCoordinates().get(0);

        Coordinate[] coordinates= ring.stream()
                .map(p -> new Coordinate(
                        p.get(0),
                        p.get(1)
                ))
                .toArray(Coordinate[]::new);
        LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
        return geometryFactory.createPolygon(linearRing);
    }
}
