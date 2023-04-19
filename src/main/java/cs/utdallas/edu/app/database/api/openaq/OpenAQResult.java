package cs.utdallas.edu.app.database.api.openaq;

public class OpenAQResult {
    private String location;
    private OpenAQParameter[] parameters;

    public String getLocation() {
        return location;
    }

    public OpenAQParameter[] getParameters() {
        return parameters;
    }
}
