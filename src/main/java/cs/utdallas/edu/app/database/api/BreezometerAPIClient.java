package cs.utdallas.edu.app.database.api;

import cs.utdallas.edu.app.database.PollutantType;

import java.util.Collection;
import java.util.List;

import static cs.utdallas.edu.app.database.PollutantType.*;

public final class BreezometerAPIClient implements APIClient {
    @Override
    public Collection<PollutantType> getSupportedPollutants() {
        return List.of(CO, NO2, O3, PM2_5, PM10, SO2);
    }
}
