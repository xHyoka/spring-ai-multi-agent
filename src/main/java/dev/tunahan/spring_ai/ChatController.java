package dev.tunahan.spring_ai;


import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;
    private final ChatClient researcherClient;


    public ChatController(ChatClient.Builder builder,
                          ChatMemory chatMemory,
                          @Qualifier("researcherClient") ChatClient researcherClient) {

        this.chatClient = builder.
                defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
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


    @PostMapping("/time")
    public String time(@Valid @RequestBody ChatRequest request){
        return chatClient.prompt()
                .user(request.message())
                .tools(new DateTimeTools())
                .call()
                .content();
    }

    @PostMapping("/city")
    public String city(@Valid @RequestBody ChatRequest request){
        return chatClient.prompt()
                .user(request.message())
                .tools(new CityTools(),new DateTimeTools())
                .call()
                .content();
    }

    @PostMapping("/agent")
    public String agent(@Valid @RequestBody ChatRequest request){
        return chatClient.prompt()
                .system("""
                        Sen bir baş asistansın. Şehir verisi (nüfus, yüzölçümü, hava durumu) gereken HER soruda önce research aracını çağır.
                        Kendi bilginle sayı söyleme. Araçtan gelen veriyle cevap yaz.
                        Araç cevabında HATA varsa kullanıcıya verinin şu an alınamadığını söyle ve tahmin yapma.
                        Araç cevabında Bu şehir hakkında veri yok varsa hangi bilginin bulunamadığını açıkca söyle,
                        bulunan verilerle cevabın kalanını yaz.
                        Cevabı kısa ve Türkçe ver.
                """)
                .user(request.message())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID,request.conversationId()))
                .tools(new ResearchAgentTool(researcherClient))
                .call()
                .content();
    }

}
