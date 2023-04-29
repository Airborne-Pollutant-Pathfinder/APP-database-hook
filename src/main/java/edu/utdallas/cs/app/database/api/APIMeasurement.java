package edu.utdallas.cs.app.database.api;

import edu.utdallas.cs.app.database.PollutantType;

import java.util.Date;

public final class APIMeasurement {
    public static APIMeasurement of(PollutantType pollutantType, Date dateTime, double value) {
        return new APIMeasurement(pollutantType, dateTime, value);
    }

    private final PollutantType pollutantType;

    private final Date dateTime;

    private final double value;

    private APIMeasurement(PollutantType pollutantType, Date dateTime, double value) {
        this.pollutantType = pollutantType;
        this.dateTime = dateTime;
        this.value = value;
    }

    public PollutantType getPollutantType() {
        return pollutantType;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public double getValue() {
        return value;
    }
}
