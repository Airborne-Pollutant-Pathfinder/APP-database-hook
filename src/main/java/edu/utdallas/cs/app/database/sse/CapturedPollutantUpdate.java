package edu.utdallas.cs.app.database.sse;

import edu.utdallas.cs.app.database.table.CapturedPollutant;

import java.util.Date;

public class CapturedPollutantUpdate {
    public static CapturedPollutantUpdate fromDatabaseRow(CapturedPollutant pollutant) {
        return new CapturedPollutantUpdate(
                pollutant.getSensor().getId(),
                pollutant.getPollutant().getId(),
                pollutant.getDatetime(),
                pollutant.getValue());
    }

    private int sensorId;
    private int pollutantId;
    private Date datetime;
    private double value;

    public CapturedPollutantUpdate(int sensorId, int pollutantId, Date datetime, double value) {
        this.sensorId = sensorId;
        this.pollutantId = pollutantId;
        this.datetime = datetime;
        this.value = value;
    }

    public CapturedPollutantUpdate() {
    }

    public int getSensorId() {
        return sensorId;
    }

    public int getPollutantId() {
        return pollutantId;
    }

    public Date getDatetime() {
        return datetime;
    }

    public double getValue() {
        return value;
    }
}
