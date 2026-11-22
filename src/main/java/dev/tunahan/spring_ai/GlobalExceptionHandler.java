package dev.tunahan.spring_ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler   {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AgentException.class)
    public ProblemDetail handleAgent(AgentException e){
        log.error("Ajan hatası [{}]: {}", e.getAgentName(), e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Ajan şuan cevap veremiyor,lütfen sonra tekrar dene.");
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e){
        log.error("Beklenmeyen hata: {}", e.getMessage(), e);
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Beklenmeyen bir hata oluştu.");
    }
}
