package cs.utdallas.edu.app.database.api.openaq;

import com.google.gson.Gson;
import cs.utdallas.edu.app.database.PollutantType;
import cs.utdallas.edu.app.database.api.APIClient;
import cs.utdallas.edu.app.database.api.APISource;
import cs.utdallas.edu.app.database.table.Sensor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static cs.utdallas.edu.app.database.PollutantType.*;

public final class OpenAQAPIClient implements APIClient {
    @Override
    public void fetchData(long since, Sensor sensor, String pollutant) throws IOException {
        if (sensor.getSource() != APISource.OPENAQ) {
            return;
        }

        HttpUrl url = createHttpUrl(sensor);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            String jsonResponse = response.body().string();
            System.out.println(jsonResponse);

            Gson gson = new Gson();
            OpenAQResponse openAQResponse = gson.fromJson(jsonResponse, OpenAQResponse.class);
            OpenAQResult result = openAQResponse.getResults()[0];
            OpenAQParameter pm25 = null;
            for (OpenAQParameter measurement : result.getParameters()) {
                if (measurement.getParameter().equals("pm25")) {
                    pm25 = measurement;
                    break;
                }
            }

            if (pm25 != null) {
                double pm25Value = pm25.getCount();
                String unit = pm25.getUnit();
                String lastUpdated = pm25.getLastUpdated();
                System.out.printf(
                        "Latest PM2.5 readings for location %s: %f %s (%s)\n",
                        sensor.getSourceId(), pm25Value, unit, lastUpdated);
            }
        }
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
