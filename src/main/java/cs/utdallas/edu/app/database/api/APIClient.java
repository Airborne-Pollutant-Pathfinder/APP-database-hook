package cs.utdallas.edu.app.database.api;

import cs.utdallas.edu.app.database.PollutantType;
import cs.utdallas.edu.app.database.table.Sensor;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface APIClient {
    Collection<PollutantType> getSupportedPollutants();

    Optional<APIReading> fetchData(long since, Sensor sensor, PollutantType pollutant) throws IOException;
}
