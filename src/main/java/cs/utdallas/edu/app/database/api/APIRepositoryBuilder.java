package cs.utdallas.edu.app.database.api;

import java.util.*;

public final class APIRepositoryBuilder {
    private final Map<String, Collection<APIClient>> apiClientsForPollutant = new HashMap<>();

    public APIRepositoryBuilder register(String pollutant, APIClient client) {
        apiClientsForPollutant.putIfAbsent(pollutant, new ArrayList<>());
        apiClientsForPollutant.get(pollutant).add(client);
        return this;
    }

    public APIRepositoryBuilder registerAllSupportedPollutants(APIClient client) {
        client.getSupportedPollutants().forEach(pollutant -> register(pollutant.toString(), client));
        return this;
    }

    public APIRepository build() {
        return new APIRepositoryImpl(apiClientsForPollutant);
    }
}
