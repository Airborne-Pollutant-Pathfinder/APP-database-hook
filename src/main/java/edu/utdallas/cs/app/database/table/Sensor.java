package edu.utdallas.cs.app.database.table;

import edu.utdallas.cs.app.database.api.APISource;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "sensor")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_id")
    private int id;

    @Column(name = "location", columnDefinition = "Point")
    private Point location;

    @Column(name = "radius_meters")
    private double radiusMeters;

    @Column(name = "area", columnDefinition = "Geometry")
    private Geometry area;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private APISource source;

    @Column(name = "source_id")
    private String sourceId;

    public int getId() {
        return id;
    }

    public Point getLocation() {
        return location;
    }

    public double getRadiusMeters() {
        return radiusMeters;
    }

    public Geometry getArea() {
        return area;
    }

    public APISource getSource() {
        return source;
    }

    public String getSourceId() {
        return sourceId;
    }
}