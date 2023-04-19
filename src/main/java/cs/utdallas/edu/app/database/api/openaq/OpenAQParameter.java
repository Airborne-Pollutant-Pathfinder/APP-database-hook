package cs.utdallas.edu.app.database.api.openaq;

import cs.utdallas.edu.app.database.PollutantType;

public class OpenAQParameter {
    private transient PollutantType pollutantType;
    private double lastValue;
    private String unit;
    private String lastUpdated;

    public PollutantType getPollutantType() {
        return pollutantType;
    }

    public void setPollutantType(PollutantType pollutantType) {
        this.pollutantType = pollutantType;
    }

    public double getLastValue() {
        return lastValue;
    }

    public void setLastValue(double lastValue) {
        this.lastValue = lastValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
