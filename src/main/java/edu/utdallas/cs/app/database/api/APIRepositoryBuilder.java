package edu.utdallas.cs.app.database.api;

import edu.utdallas.cs.app.database.PollutantType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class APIRepositoryBuilder {
    private final Map<PollutantType, Collection<APIAdapter>> apiClientsForPollutant = new HashMap<>();

    public APIRepositoryBuilder register(PollutantType pollutant, APIAdapter client) {
        apiClientsForPollutant.putIfAbsent(pollutant, new ArrayList<>());
        apiClientsForPollutant.get(pollutant).add(client);
        return this;
    }

    public APIRepositoryBuilder registerAllSupportedPollutants(APIAdapter client) {
        client.getSupportedPollutants().forEach(pollutant -> register(pollutant, client));
        return this;
    }

    public APIRepository build() {
        return new APIRepositoryImpl(apiClientsForPollutant);
    }
}
