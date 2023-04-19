package cs.utdallas.edu.app.database.api;

import cs.utdallas.edu.app.database.PollutantType;

import java.util.*;

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
