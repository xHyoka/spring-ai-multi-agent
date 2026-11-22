package dev.tunahan.spring_ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(
        @NotBlank(message = "Mesaj boş olamaz")
        @Size(max = 1000, message = "Mesaj en fazla 1000 karakter olabilir")
        String message,
        @NotBlank(message = "Konuşma kimliği boş olamaz")
        String conversationId
) {}