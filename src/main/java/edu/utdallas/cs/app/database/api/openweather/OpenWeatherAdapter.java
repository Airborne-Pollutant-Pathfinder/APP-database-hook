package edu.utdallas.cs.app.database.api.openweather;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.model.Coordinate;
import com.github.prominence.openweathermap.api.model.air.pollution.AirPollutionDetails;
import com.github.prominence.openweathermap.api.model.air.pollution.AirPollutionRecord;
import edu.utdallas.cs.app.database.PollutantType;
import edu.utdallas.cs.app.database.api.APIMeasurement;
import edu.utdallas.cs.app.database.api.CachedAPIAdapter;
import edu.utdallas.cs.app.database.table.Sensor;
import edu.utdallas.cs.app.database.util.ConversionUtil;

import java.io.IOException;
import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static edu.utdallas.cs.app.database.PollutantType.*;

public class OpenWeatherAdapter extends CachedAPIAdapter {

    private final OpenWeatherMapClient client;

    public OpenWeatherAdapter(String apiToken) {
        client = new OpenWeatherMapClient(apiToken);
    }

    @Override
    public Optional<APIMeasurement> fetchData(Sensor sensor, PollutantType pollutant) throws IOException {
        if (cacheContains(sensor, pollutant)) {
            return Optional.of(getCachedValue(sensor, pollutant));
        }

        AirPollutionDetails details = client
                .airPollution()
                .current()
                .byCoordinate(Coordinate.of(sensor.getLocation().getY(), sensor.getLocation().getX()))
                .retrieve()
                .asJava();
        if (details.getAirPollutionRecords().isEmpty()) {
            throw new IOException("Blank data returned by OpenWeatherMap API for sensor " + sensor.getId() + " and pollutant " + pollutant + ".");
        }
        AirPollutionRecord record = details.getAirPollutionRecords().get(0);
        cacheValues(sensor, record);
        return Optional.of(getCachedValue(sensor, pollutant));
    }

    private void cacheValues(Sensor sensor, AirPollutionRecord record) {
        for (PollutantType supportedPollutant : getSupportedPollutants()) {
            double value = getPollutantLevel(supportedPollutant, record);
            APIMeasurement measurement = APIMeasurement.of(supportedPollutant, Date.from(Instant.now(Clock.systemUTC())), value);
            cacheValue(sensor, supportedPollutant, measurement);
        }
    }

    private double getPollutantLevel(PollutantType pollutant, AirPollutionRecord record) {
        return switch (pollutant) {
            case CO -> ConversionUtil.convertCO(record.getCO());
            case NO2 -> ConversionUtil.convertNO2(record.getNO2());
            case O3 -> ConversionUtil.convertO3(record.getO3());
            case PM2_5 -> record.getPM2_5();
            case PM10 -> record.getPM10();
            case SO2 -> ConversionUtil.convertSO2(record.getSO2());
        };
    }

    @Override
    public Collection<PollutantType> getSupportedPollutants() {
        return List.of(CO, NO2, O3, PM2_5, PM10, SO2);
    }
}
