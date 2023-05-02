package edu.utdallas.cs.app.database.api;

import edu.utdallas.cs.app.database.PollutantType;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public final class APIRepositoryImpl implements APIRepository {
    private final Map<PollutantType, Collection<APIAdapter>> apiClientsForPollutant;

    public APIRepositoryImpl(Map<PollutantType, Collection<APIAdapter>> apiClientsForPollutant) {
        this.apiClientsForPollutant = apiClientsForPollutant;
    }

    @Override
    public Collection<APIAdapter> getAdapters(PollutantType pollutant) {
        return apiClientsForPollutant.getOrDefault(pollutant, Collections.emptyList());
    }

    @Override
    public void clearCachedAdapters() {
        apiClientsForPollutant.values().stream()
                .flatMap(Collection::stream)
                .filter(adapter -> adapter instanceof CachedAPIAdapter)
                .map(adapter -> (CachedAPIAdapter) adapter)
                .forEach(CachedAPIAdapter::clearCache);
    }

    @Override
    public Collection<PollutantType> getSupportedPollutants() {
        return apiClientsForPollutant.keySet();
    }
}
