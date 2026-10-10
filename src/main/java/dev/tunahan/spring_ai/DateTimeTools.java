package dev.tunahan.spring_ai;

import org.springframework.ai.tool.annotation.Tool;

import java.time.LocalDateTime;

public class DateTimeTools {

    @Tool(description = "Şu anki tarih ve saati döner")
    public String currentDateTime(){
        return LocalDateTime.now().toString();
    }
}
