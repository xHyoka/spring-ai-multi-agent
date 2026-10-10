package dev.tunahan.spring_ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class ResearchAgentTool {

    private final ChatClient researcher;

    public ResearchAgentTool(ChatClient researcher) {
        this.researcher = researcher;
    }

    @Tool(description = "Şehirler hakkında veri araştırır (nüfus gibi).Şehirle ilgili her soru için bu ajanı kullan ")
    public String search(@ToolParam(description = "Araştırılacak konu, örn: Ankara ve İzmir nüfusu") String topic) {
        System.out.println("Alt agent çalıştı: research(" + topic + ")");
        try{
        String result = researcher.prompt()
                .system("""
                        Sen bir veri araştırmacısısın.
                        Nüfus için cityPopulation, yüzölçümü için cityArea aracını kullan.
                        Birden fazla şehir varsa her şehir için aracı ayrı çağır.
                        Araçtan gelen veriyi aynen aktar, asla tahmin yapma.
                        Bir araç Bu şehir için veri yok dönerse o şehir ve bilgi için Bu şehir için veri yok yaz.
                        Bulduğun verileri ve bulamadıklarını ayrı ayrı belirt.
                        """)
                .user(topic)
                .tools(new CityTools())
                .call()
                .content();

        if (result == null || result.isBlank()) {
            return "HATA: Araştırma ajanı boş cevap döndü";
        }
        return result;
    }

        catch (Exception e){
            System.out.println("Alt agent hatası: " e.getMessage());
            return "HATA: Araştırma ajanı çalışırken sorun oluştu (" + e.getClass().getSimpleName() + ").";
        }
    }
}
