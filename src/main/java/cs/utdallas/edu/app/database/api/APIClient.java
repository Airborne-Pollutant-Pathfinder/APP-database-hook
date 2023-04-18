package cs.utdallas.edu.app.database.api;

import cs.utdallas.edu.app.database.PollutantType;

import java.util.Collection;

public interface APIClient {
    Collection<PollutantType> getSupportedPollutants();
}
