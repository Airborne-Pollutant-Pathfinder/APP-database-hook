package edu.utdallas.cs.app.database.api;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import edu.utdallas.cs.app.database.PollutantType;
import edu.utdallas.cs.app.database.table.Sensor;

public abstract class CachedAPIAdapter implements APIAdapter {
    private final Table<Sensor, PollutantType, APIMeasurement> cache = HashBasedTable.create();

    protected boolean cacheContains(Sensor sensor, PollutantType type) {
        return cache.contains(sensor, type);
    }

    protected APIMeasurement getCachedValue(Sensor sensor, PollutantType type) {
        return cache.get(sensor, type);
    }

    protected void cacheValue(Sensor sensor, PollutantType type, APIMeasurement measurement) {
        cache.put(sensor, type, measurement);
    }

    public void clearCache() {
        cache.clear();
    }
}
