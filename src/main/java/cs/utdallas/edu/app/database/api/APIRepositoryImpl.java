package cs.utdallas.edu.app.database.api;

import java.util.*;

public final class APIRepositoryImpl implements APIRepository {
    private final Map<String, Collection<APIClient>> apiClientsForPollutant;

    public APIRepositoryImpl(Map<String, Collection<APIClient>> apiClientsForPollutant) {
        this.apiClientsForPollutant = apiClientsForPollutant;
    }

    @Override
    public Collection<APIClient> getClients(String pollutant) {
        return apiClientsForPollutant.getOrDefault(pollutant, Collections.emptyList());
    }

    @Override
    public Collection<String> getSupportedPollutants() {
        return apiClientsForPollutant.keySet();
    }
}
