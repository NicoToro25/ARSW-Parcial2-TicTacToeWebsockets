package edu.eci.arsw.TicTacToe.model;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game {

    // Atributos
    private String id;
    private Map<String, Player> players;
    private String[] board;
    private String currentPlayer;
    private String winner;
    private boolean gameOver;

    // Constructor
    public Game(String id) {
        this.id = id;
        this.players = new ConcurrentHashMap<>();
        this.board = new String[9];
        Arrays.fill(this.board, null);
        this.currentPlayer = "X";
        this.winner = null;
        this.gameOver = false;
    }

    // Getters
    public String getId() { return id; }
    public Map<String, Player> getPlayers() { return players; }
    public String[] getBoard() {
        if (board == null) {
            board = new String[9];
            Arrays.fill(board, null);
        }
        return board;
    }
    public String getCurrentPlayer() {
        return currentPlayer != null ? currentPlayer : "X";
    }
    public String getWinner() { return winner; }
    public boolean isGameOver() { return gameOver; }
    public int getPlayerCount() { return players.size(); }

    // Este método añade jugadores y le asigna un simbolo ("X" o "O")
    public synchronized boolean addPlayer(Player player){
        if (players.size() >= 2)
            return false;
        players.put(player.getId(), player);
        if (players.size() == 1) {
            player.setSymbol("X");
        } else {
            player.setSymbol("O");
        }
        return true;
    }

    // Este método quita jugadores
    public synchronized void removePlaver(String playerId) {
        players.remove(playerId);
    }

    // Este método verifica el movimiento que el jugador va a realizar
    public synchronized boolean makeMove(String playerId, int position){
        if (gameOver || board[position] != null)
            return false;

        Player player = players.get(playerId);
        if (player == null || !player.getSymbol().equals(currentPlayer))
            return false;

        board[position] = player.getSymbol();
        checkWinner();

        if (!gameOver) {
            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
        }
        return true;
    }

    // Este método verifica el ganador (se da un array con las posibles formas de ganar)
    public void checkWinner(){
        int[][] lines = {
                {0,1,2}, {3,4,5}, {6,7,8}, {0,3,6},
                {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}
        };

        for (int[] line : lines) {
            if(board[line[0]] != null && board[line[0]].equals(board[line[1]]) && board[line[0]].equals(board[line[2]])) {
                winner = board[line[0]];
                gameOver = true;
            }
        }
    }

    // Reinicia el juego
    public synchronized void reset() {
        Arrays.fill(board, null);
        currentPlayer = "X";
        winner = null;
        gameOver = false;
    }

}
