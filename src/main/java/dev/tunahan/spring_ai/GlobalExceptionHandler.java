package dev.tunahan.spring_ai;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler   {

    @ExceptionHandler(AgentException.class)
    public ProblemDetail handleAgent(AgentException e){
        System.out.println("Ajan hatası [" e.getAgentName() + "]: " + e.getMessage());
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Ajan şuan cevap veremiyor,lütfen sonra tekrar dene.");
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e){
        System.out.println("Beklenmeyen hata: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Beklenmeyen bir hata oluştu.");
    }
}
