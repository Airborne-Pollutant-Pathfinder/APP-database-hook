package edu.utdallas.cs.app.database.api;

import edu.utdallas.cs.app.database.PollutantType;
import edu.utdallas.cs.app.database.table.Sensor;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface APIClient {
    Optional<APIReading> fetchData(long since, Sensor sensor, PollutantType pollutant) throws IOException;

    Collection<PollutantType> getSupportedPollutants();
}
