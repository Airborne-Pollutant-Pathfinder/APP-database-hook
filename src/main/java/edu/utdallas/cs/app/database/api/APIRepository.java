package edu.utdallas.cs.app.database.api;

import edu.utdallas.cs.app.database.PollutantType;

import java.util.Collection;

public interface APIRepository {
    static APIRepositoryBuilder builder() {
        return new APIRepositoryBuilder();
    }

    Collection<APIClient> getClients(PollutantType pollutant);

    Collection<PollutantType> getSupportedPollutants();
}
