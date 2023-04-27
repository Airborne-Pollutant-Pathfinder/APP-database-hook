package edu.utdallas.cs.app.database.api.mints;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import edu.utdallas.cs.app.database.Main;
import edu.utdallas.cs.app.database.PollutantType;
import edu.utdallas.cs.app.database.api.APIAdapter;
import edu.utdallas.cs.app.database.api.APIReading;
import edu.utdallas.cs.app.database.api.APISource;
import edu.utdallas.cs.app.database.table.Sensor;
import edu.utdallas.cs.app.database.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static edu.utdallas.cs.app.database.PollutantType.PM10;
import static edu.utdallas.cs.app.database.PollutantType.PM2_5;

public class MINTSAdapter implements APIAdapter {
    private static final BiMap<PollutantType, String> POLLUTANT_TO_FIELD = ImmutableBiMap.<PollutantType, String>builder()
            .put(PM2_5, "pm2_5")
            .put(PM10, "pm10_0")
            .build();
    private static final String MEASUREMENT = "IPS7100";

    private final String bucket;
    private final InfluxDBClient client;
    private final DateTimeFormatter formatter;

    public MINTSAdapter(String bucket, String org, String token, String url) {
        this.bucket = bucket;
        client = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'");
    }

    @Override
    public Optional<APIReading> fetchData(long sinceMs, Sensor sensor, PollutantType pollutant) {
        if (sensor.getSource() != APISource.MINTS) {
            return Optional.empty();
        }

        long minutesSince = calculateMinutesSince(sinceMs);

        System.out.println("minutesSince = " + minutesSince);

        String query = String.format("from(bucket: \"%s\")\n"
                + "  |> range(start: -" + minutesSince + "m)\n"
                + "  |> filter(fn: (r) => r[\"_measurement\"] == \"" + MEASUREMENT + "\")\n"
                + "  |> filter(fn: (r) => r[\"_field\"] == \"" + POLLUTANT_TO_FIELD.get(pollutant) + "\")\n"
                + "  |> filter(fn: (r) => r[\"device_id\"] == \"%s\")\n"
                + "  |> mean()", bucket, sensor.getSourceId());

        List<FluxTable> tables = client.getQueryApi().query(query);

        // We expect only 1 table and 1 record based on the query
        if (tables.isEmpty()) {
            return Optional.empty();
        }

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                System.out.println("record = " + record);
                System.out.println("record.getValues() = " + record.getValues());
            }
        }

        FluxTable table = tables.get(0);

        if (table.getRecords().isEmpty()) {
            return Optional.empty();
        }

        FluxRecord record = table.getRecords().get(0);
        Map<String, Object> values = record.getValues();

        if (!values.containsKey("_value")) {
            return Optional.empty();
        }

        try {
            double value = Double.parseDouble(values.get("_value").toString());
            Date date = DateUtil.createDate(formatter, values.get("_stop").toString());
            System.out.println("date = " + date);
            return Optional.of(APIReading.of(pollutant, date, value));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private long calculateMinutesSince(long sinceMs) {
        if (sinceMs == 0) {
            return Main.MINUTES_TO_FETCH;
        }
        Instant instant1 = Instant.ofEpochMilli(System.currentTimeMillis());
        Instant instant2 = Instant.ofEpochMilli(sinceMs);
        // we need at least one minute, since 0 minutes is an invalid query
        return Math.max(1, ChronoUnit.MINUTES.between(instant1, instant2));
    }

    @Override
    public Collection<PollutantType> getSupportedPollutants() {
        return List.of(PM2_5, PM10);
    }
}
