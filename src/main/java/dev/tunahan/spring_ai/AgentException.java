package dev.tunahan.spring_ai;

public class AgentException extends RuntimeException{
    private final String agentName;

    public AgentException(String agentName,String message,Throwable cause){
        super(message,cause);
        this.agentName = agentName;
    }

    public String getAgentName(){
        return agentName;
    }
}
