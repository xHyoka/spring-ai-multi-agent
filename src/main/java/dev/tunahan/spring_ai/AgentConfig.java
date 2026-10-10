package dev.tunahan.spring_ai;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.openai.OpenAiChatOptions;

@Configuration
public class AgentConfig {


    @Bean
    public ChatClient researcherClient(@Value("${gemini.api-key}")String apiKey,
                                       @Value("${gemini.model}")String model){
        OpenAiChatModel geminiModel = OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .baseUrl("https://generativelanguage.googleapis.com/v1beta/openai")
                        .apiKey(apiKey)
                        .model(model)
                        .temperature(0.2)
                        .build())
                .build();

        return ChatClient.builder(geminiModel).build();
    }


}
