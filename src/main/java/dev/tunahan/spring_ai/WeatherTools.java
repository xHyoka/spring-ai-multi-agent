package dev.tunahan.spring_ai;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;


public class WeatherTools {
    private static final Logger log = LoggerFactory.getLogger(WeatherTools.class);
    private final RestClient restClient;

    public WeatherTools() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // bağlantı kurmak için en fazla 3 sn
        factory.setReadTimeout(5000);    // cevap için en fazla 5 sn
        this.restClient = RestClient.builder().requestFactory(factory).build();
    }

    @Tool(description = "Verilen şehrin anlık hava durumunu döner: sıcaklık, nem, rüzgar.")
    public String currentWeather(@ToolParam(description = "Şehir adı, örn: İzmir") String city) {
        log.info("Tool çağrıldı: currentWeather({})", city);
        try {
            JsonNode place = findCity(city);
            if (place == null) {
                return "Bu şehir için veri yok: " + city + " bulunamadı.";
            }

            double lat = place.get("latitude").asDouble();
            double lon = place.get("longitude").asDouble();


            JsonNode weather = restClient.get()
                    .uri("https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}"
                            + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m", lat, lon)
                    .retrieve()
                    .body(JsonNode.class);


            JsonNode current = weather.get("current");
            return "%s: %.1f °C, nem %%%d, rüzgar %.1f km/s".formatted(
                    place.get("name").asText(),
                    current.get("temperature_2m").asDouble(),
                    current.get("relative_humidity_2m").asInt(),
                    current.get("wind_speed_10m").asDouble());

        }catch (RestClientException e){
            log.error("Hava durumu servisine ulaşılamadı {}",city,e);
            return "HATA: Hava durumu servisine şu an ulaşılamıyor.";
        }
    }


    private JsonNode findCity(String city) {
        JsonNode response = restClient.get()
                .uri("https://geocoding-api.open-meteo.com/v1/search?name={name}&count=1&language=tr", city)
                .retrieve()
                .body(JsonNode.class);

        JsonNode results = response.get("results");
        return (results == null || results.isEmpty()) ? null : results.get(0);
    }


}
