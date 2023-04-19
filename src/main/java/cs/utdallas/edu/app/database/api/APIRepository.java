package cs.utdallas.edu.app.database.api;

import cs.utdallas.edu.app.database.PollutantType;

import java.util.Collection;

public interface APIRepository {
    static APIRepositoryBuilder builder() {
        return new APIRepositoryBuilder();
    }

    Collection<APIClient> getClients(PollutantType pollutant);

    Collection<PollutantType> getSupportedPollutants();
}
