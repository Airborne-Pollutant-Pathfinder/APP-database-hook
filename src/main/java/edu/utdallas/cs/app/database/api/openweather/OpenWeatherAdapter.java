package edu.utdallas.cs.app.database.api.openweather;

import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.model.Coordinate;
import com.github.prominence.openweathermap.api.model.air.pollution.AirPollutionDetails;
import com.github.prominence.openweathermap.api.model.air.pollution.AirPollutionRecord;
import com.github.prominence.openweathermap.api.request.air.pollution.AirPollutionRequester;
import edu.utdallas.cs.app.database.PollutantType;
import edu.utdallas.cs.app.database.api.APIAdapter;
import edu.utdallas.cs.app.database.api.APIMeasurement;
import edu.utdallas.cs.app.database.table.Sensor;

import java.io.IOException;
import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static edu.utdallas.cs.app.database.PollutantType.*;

public class OpenWeatherAdapter implements APIAdapter {

    private final AirPollutionRequester requester;

    public OpenWeatherAdapter(String apiToken) {
        requester = new OpenWeatherMapClient(apiToken).airPollution();
    }

    @Override
    public Optional<APIMeasurement> fetchData(Sensor sensor, PollutantType pollutant) throws IOException {
        AirPollutionDetails details = requester.current()
                .byCoordinate(Coordinate.of(sensor.getLocation().getY(), sensor.getLocation().getX()))
                .retrieve()
                .asJava();
        if (details.getAirPollutionRecords().isEmpty()) {
            throw new IOException("Blank data returned by OpenWeatherMap API for sensor " + sensor.getId() + " and pollutant " + pollutant + ".");
        }
        AirPollutionRecord record = details.getAirPollutionRecords().get(0);
        double value = getPollutantLevel(pollutant, record);
        return Optional.of(APIMeasurement.of(pollutant, Date.from(Instant.now(Clock.systemUTC())), value));
    }

    private double getPollutantLevel(PollutantType pollutant, AirPollutionRecord record) {
        return switch (pollutant) {
            case CO -> record.getCO();
            case NO2 -> record.getNO2();
            case O3 -> record.getO3();
            case PM2_5 -> record.getPM2_5();
            case PM10 -> record.getPM10();
            case SO2 -> record.getSO2();
        };
    }

    @Override
    public Collection<PollutantType> getSupportedPollutants() {
        return List.of(CO, NO2, O3, PM2_5, PM10, SO2);
    }
}
