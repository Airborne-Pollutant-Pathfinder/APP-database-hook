package edu.utdallas.cs.app.database.sse;

import java.util.List;

public class SensorUpdate {
    public static SensorUpdate of(int sensorId, List<CapturedPollutantUpdate> pollutants) {
        return new SensorUpdate(sensorId, pollutants);
    }

    private int sensorId;
    private List<CapturedPollutantUpdate> pollutants;

    public SensorUpdate(int sensorId, List<CapturedPollutantUpdate> pollutants) {
        this.sensorId = sensorId;
        this.pollutants = pollutants;
    }

    public SensorUpdate() {
    }

    public int getSensorId() {
        return sensorId;
    }

    public List<CapturedPollutantUpdate> getPollutants() {
        return pollutants;
    }
}
