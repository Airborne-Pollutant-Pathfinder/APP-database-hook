package cs.utdallas.edu.app.database.api;

import cs.utdallas.edu.app.database.PollutantType;
import cs.utdallas.edu.app.database.table.Sensor;

import java.io.IOException;
import java.util.Collection;

public interface APIClient {
    Collection<PollutantType> getSupportedPollutants();

    void fetchData(long since, Sensor sensor, String pollutant) throws IOException;
}
