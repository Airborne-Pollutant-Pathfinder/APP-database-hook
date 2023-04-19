package edu.utdallas.cs.app.database.api;

import edu.utdallas.cs.app.database.PollutantType;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public final class APIRepositoryImpl implements APIRepository {
    private final Map<PollutantType, Collection<APIClient>> apiClientsForPollutant;

    public APIRepositoryImpl(Map<PollutantType, Collection<APIClient>> apiClientsForPollutant) {
        this.apiClientsForPollutant = apiClientsForPollutant;
    }

    @Override
    public Collection<APIClient> getClients(PollutantType pollutant) {
        return apiClientsForPollutant.getOrDefault(pollutant, Collections.emptyList());
    }

    @Override
    public Collection<PollutantType> getSupportedPollutants() {
        return apiClientsForPollutant.keySet();
    }
}
