package edu.utdallas.cs.app.database.sse;

import java.util.List;

public class SensorUpdate {
    public static SensorUpdate of(int sensorId, double radiusMeters, List<CapturedPollutantUpdate> pollutants) {
        return new SensorUpdate(sensorId, radiusMeters, pollutants);
    }

    private int sensorId;
    private double radiusMeters;
    private List<CapturedPollutantUpdate> pollutants;

    public SensorUpdate(int sensorId, double radiusMeters, List<CapturedPollutantUpdate> pollutants) {
        this.sensorId = sensorId;
        this.radiusMeters = radiusMeters;
        this.pollutants = pollutants;
    }

    public SensorUpdate() {
    }

    public int getSensorId() {
        return sensorId;
    }

    public double getRadiusMeters() {
        return radiusMeters;
    }

    public List<CapturedPollutantUpdate> getPollutants() {
        return pollutants;
    }
}
