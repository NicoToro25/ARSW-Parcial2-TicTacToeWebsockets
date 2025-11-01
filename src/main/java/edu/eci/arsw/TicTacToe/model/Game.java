package edu.eci.arsw.TicTacToe.model;

import java.util.Map;

public class Game {

    private String id;
    private Map<String, Player> players;
    private String[] board;
    private String currentPlayer;
    private String winner;
    private boolean gameOver;

    public Game(String id, Map<String, Player> players, String[] board, String currentPlayer, String winner, boolean gameOver) {
        this.id = id;
        this.players = players;
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.winner = winner;
        this.gameOver = gameOver;
    }

    // Acá se va a implementar toda la lógica de los hilos

}
