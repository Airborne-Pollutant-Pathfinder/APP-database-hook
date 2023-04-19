package cs.utdallas.edu.app.database.api;

import cs.utdallas.edu.app.database.PollutantType;

import java.util.Date;

public final class APIReading {
    public static APIReading of(PollutantType pollutantType, Date dateTime, double value) {
        return new APIReading(pollutantType, dateTime, value);
    }

    private final PollutantType pollutantType;

    private final Date dateTime;

    private final double value;

    private APIReading(PollutantType pollutantType, Date dateTime, double value) {
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
