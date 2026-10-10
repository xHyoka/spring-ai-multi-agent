package dev.tunahan.spring_ai;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.Locale;
import java.util.Map;

public class CityTools {

    private static final Map<String,Integer> NUFUS = Map.of(
            "istanbul",15800000,
            "ankara",5700000,
            "izmir",4400000
    );

    private static final Map<String, Integer> YUZOLCUMU = Map.of(
            "istanbul", 5343,
            "ankara", 25632,
            "izmir", 11891
    );

    @Tool(description = "Verilen şehrin yaklaşık nüfusunu döner.Şehir adını küçük harfle,Türkçe karakter olmadan ver.")
    public String cityPopulation(@ToolParam(description = "Şehir adı,örn:istanbul") String city){
        System.out.println("Tool çağırıldı: cityPopulation:(" + city + ")");
        Integer pop = NUFUS.get(normalize(city) );
        return pop == null ? "Bu şehir için veri yok" : String.valueOf(pop);
    }

    @Tool(description = "Verilen şehrin yaklaşık yüzölçümünü döner.Şehrin adını küçük harfle,Türkçe karakter olmadan ver.")
    public String cityArea(@ToolParam(description = "Şehir adı,örn:istanbul") String city){
        System.out.println("Tool çağırıldı: cityPopulation:(" + city + ")");
        Integer area = YUZOLCUMU.get(normalize(city));
        return area == null ? "Bu şehir için veri yok," + city  + "için yüz ölçümü bulunamıyor." : area + "km^2";
    }

    private String normalize(String s){
        return s.trim()
                .toLowerCase(Locale.forLanguageTag("tr"))
                .replace('ı','i').replace('ğ','g').replace('ü','u')
                .replace('ş','s').replace('ö','o').replace('ç','c');
    }
}
