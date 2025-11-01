package edu.eci.arsw.TicTacToe.model;

public class Player {
    private String id;
    private String sessionId;
    private String symbol;

    public Player(){
    }
    public Player(String id, String sessionId) {
        this.id = id;
        this.sessionId = sessionId;
    }

    // Getters y setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
