package cs.utdallas.edu.app.database.api;

import java.util.Collection;

public interface APIRepository {
    static APIRepositoryBuilder builder() {
        return new APIRepositoryBuilder();
    }

    Collection<APIClient> getClients(String pollutant);
}
