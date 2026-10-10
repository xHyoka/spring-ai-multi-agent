package dev.tunahan.spring_ai;



import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/youtube")
public class ChatController {

    private final ChatClient chatClient;
    private final ChatClient researcherClient;

    public ChatController(ChatClient.Builder builder,
                          @Qualifier("researcherClient") ChatClient researcherClient) {

        this.chatClient = builder.build();
        this.researcherClient = researcherClient;
    }


    @GetMapping("/popular")
    public String findPopularYouTubersByGenre(
            @RequestParam(value = "genre", defaultValue = "tech") String genre) {

        String message = """
                List 10 well-known YouTubers (individual creators, not media companies) in {genre}.
                       Do not include subscriber counts. If you are unsure about someone, leave them out.
            """;

        return chatClient.prompt()
                .user(u -> u.text(message).param("genre", genre))
                .call()
                .content();
    }


    @GetMapping("/time")
    public String time(@RequestParam(defaultValue = "Şuan saat kaç?") String message){
        return chatClient.prompt()
                .user(message)
                .tools(new DateTimeTools())
                .call()
                .content();
    }

    @GetMapping("/city")
    public String city(@RequestParam(defaultValue = "İstanbul'un nüfusu kaç?") String message){
        return chatClient.prompt()
                .user(message)
                .tools(new CityTools(),new DateTimeTools())
                .call()
                .content();
    }

    @GetMapping("/agent")
    public String agent(@RequestParam(defaultValue = "Ankara mı İzmir mi daha kalabalık?")String message){
        return chatClient.prompt()
                .system("""
                        Sen bir baş asistansın. Şehir verisi gereken HER soruda önce research aracını çağır.
                        Kendi bilginle sayı söyleme. Araçtan gelen veriyle cevap yaz.
                        Araç cevabında HATA varsa kullanıcıya verinin şu an alınamadığını söyle ve tahmin yapma.
                        Araç cevabında Bu şehir hakkında veri yok varsa hangi bilginin bulunamadığını açıkca söyle,
                        bulunan verilerle cevabın kalanını yaz.
                        Cevabı kısa ve Türkçe ver.
                """)
                .user(message)
                .tools(new ResearchAgentTool(researcherClient))
                .call()
                .content();
    }

}
