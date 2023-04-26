package edu.utdallas.cs.app.database.api.openaq;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.utdallas.cs.app.database.PollutantType;
import edu.utdallas.cs.app.database.api.APIClient;
import edu.utdallas.cs.app.database.api.APIReading;
import edu.utdallas.cs.app.database.api.APISource;
import edu.utdallas.cs.app.database.table.Sensor;
import edu.utdallas.cs.app.database.util.DateUtil;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static edu.utdallas.cs.app.database.PollutantType.PM10;
import static edu.utdallas.cs.app.database.PollutantType.PM2_5;

public final class OpenAQAPIClient implements APIClient {
    private static final BiMap<PollutantType, String> POLLUTANT_TO_FIELD = new ImmutableBiMap.Builder<PollutantType, String>()
            .put(PM2_5, "pm25")
            .put(PM10, "pm10")
            .build();

    private final OkHttpClient client;
    private final Gson gson;
    private final SimpleDateFormat dateFormat;

    public OpenAQAPIClient() {
        client = new OkHttpClient();
        gson = new GsonBuilder()
                .registerTypeAdapter(OpenAQParameter.class, new OpenAQParameterDeserializer(POLLUTANT_TO_FIELD))
                .create();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }

    @Override
    public Optional<APIReading> fetchData(long since, Sensor sensor, PollutantType pollutant) throws IOException {
        if (sensor.getSource() != APISource.OPENAQ) {
            return Optional.empty();
        }

        HttpUrl url = createHttpUrl(sensor);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            String jsonResponse = response.body().string();
            OpenAQResponse openAQResponse = gson.fromJson(jsonResponse, OpenAQResponse.class);
            OpenAQResult result = openAQResponse.getResults()[0];
            OpenAQParameter parameter = null;
            for (OpenAQParameter measurement : result.getParameters()) {
                if (pollutant == measurement.getPollutantType()) {
                    parameter = measurement;
                    break;
                }
            }

            if (parameter != null) {
                double value = parameter.getLastValue();
                Date lastUpdated = DateUtil.createDate(dateFormat, parameter.getLastUpdated());
                return Optional.of(APIReading.of(pollutant, lastUpdated, value));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    private HttpUrl createHttpUrl(Sensor sensor) {
        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.openaq.org")
                .addPathSegments("v2/locations/" + sensor.getSourceId())
                .addQueryParameter("limit", "100")
                .addQueryParameter("page", "1")
                .addQueryParameter("offset", "0")
                .addQueryParameter("sort", "desc")
                .addQueryParameter("order_by", "lastUpdated")
                .build();
    }

    @Override
    public Collection<PollutantType> getSupportedPollutants() {
        return List.of(PM2_5, PM10);
    }
}
