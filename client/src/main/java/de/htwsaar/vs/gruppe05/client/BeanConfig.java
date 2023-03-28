package de.htwsaar.vs.gruppe05.client;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class BeanConfig {

    @Bean
    public Gson gson() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

                if (json.getAsString().length() == 13) {
                    String res = json.getAsString().concat(":00:00");
                    return LocalDateTime.parse(res, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                } else {
                    return LocalDateTime.parse(json.getAsString().substring(0, 19), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                }
            }

        }).create();

        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(localDateTime));
            }
        });
        return gsonBuilder.create();
    }
}
