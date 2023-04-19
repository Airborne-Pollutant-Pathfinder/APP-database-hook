package cs.utdallas.edu.app.database.api.openaq;

public class OpenAQParameter {
    private String parameter;
    private double count;
    private String unit;
    private String lastUpdated;

    public String getParameter() {
        return parameter;
    }

    public double getCount() {
        return count;
    }

    public String getUnit() {
        return unit;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }
}
