package edu.utdallas.cs.app.database.api.openaq;

import com.google.common.collect.BiMap;
import com.google.gson.*;
import edu.utdallas.cs.app.database.PollutantType;

import java.lang.reflect.Type;

public class OpenAQParameterDeserializer implements JsonDeserializer<OpenAQParameter> {
    private final BiMap<PollutantType, String> mapping;

    public OpenAQParameterDeserializer(BiMap<PollutantType, String> mapping) {
        this.mapping = mapping;
    }

    @Override
    public OpenAQParameter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String parameter = jsonObject.get("parameter").getAsString();
        PollutantType pollutantType = mapping.inverse().getOrDefault(parameter, null);
        double lastValue = jsonObject.get("lastValue").getAsDouble();
        String unit = jsonObject.get("unit").getAsString();
        String lastUpdated = jsonObject.get("lastUpdated").getAsString();
        OpenAQParameter openAQParameter = new OpenAQParameter();
        openAQParameter.setPollutantType(pollutantType);
        openAQParameter.setLastValue(lastValue);
        openAQParameter.setUnit(unit);
        openAQParameter.setLastUpdated(lastUpdated);
        return openAQParameter;
    }
}
