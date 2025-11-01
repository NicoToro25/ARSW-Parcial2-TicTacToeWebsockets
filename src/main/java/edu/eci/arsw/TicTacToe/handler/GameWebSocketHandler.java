package edu.eci.arsw.TicTacToe.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.arsw.TicTacToe.model.Game;
import edu.eci.arsw.TicTacToe.model.Player;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@Component
public class GameWebSocketHandler implements WebSocketHandler{

    // Atributos
    private final Map<String, Game> games = new ConcurrentHashMap<>();
    private final Map<String, String> sessionGameMap = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    // Llama a un cliente cuando se conecta
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        System.out.println("Conexión establecida: " + session.getId());
    }

    // Gestiona los mensajes
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        executor.submit(() -> {
            try {
                String payload = message.getPayload().toString();
                System.out.println("Mensaje recibido: " + payload);

                Map<String, Object> data = mapper.readValue(payload, Map.class);
                String type = (String) data.get("type");

                if (type == null) {
                    sendErrorMessage(session, "Tipo de mensaje no especificado");
                    return;
                }

                switch (type) {
                    case "CREATE_GAME":
                        handleCreateGame(session);
                        break;
                    case "JOIN_GAME":
                        handleJoinGame(session, (String) data.get("gameId"));
                        break;
                    case "MAKE_MOVE":
                        handleMakeMove(session, data);
                        break;
                    case "RESET_GAME":
                        handleResetGame(session);
                        break;
                    default:
                        sendErrorMessage(session, "Tipo de mensaje desconocido: " + type);
                }
            } catch (Exception e) {
                System.err.println("Error procesando mensaje: " + e.getMessage());
                e.printStackTrace();
                try {
                    sendErrorMessage(session, "Error procesando mensaje: " + e.getMessage());
                } catch (IOException ioException) {
                    System.err.println("Error enviando mensaje de error: " + ioException.getMessage());
                }
            }
        });
    }

    // Gestiona la creación de un juego
    private void handleCreateGame(WebSocketSession session) throws IOException {
        String gameId = "game_" + System.currentTimeMillis();
        Game game = new Game(gameId);
        games.put(gameId, game);

        String playerId = "player_" + session.getId();
        Player player = new Player(playerId, session.getId());

        if (game.addPlayer(player)) {
            sessionGameMap.put(session.getId(), gameId);

            sendMessage(session, createMessage("GAME_CREATED", Map.of(
                    "gameId", gameId,
                    "playerSymbol", player.getSymbol()
            )));

            // Enviar estado inicial del juego solo a este jugador
            sendGameState(session, game);

            System.out.println("Juego creado: " + gameId + " por jugador: " + playerId);
        } else {
            sendErrorMessage(session, "No se pudo crear el juego");
        }
    }

    private void handleJoinGame(WebSocketSession session, String gameId) throws IOException {
        if (gameId == null || gameId.trim().isEmpty()) {
            sendErrorMessage(session, "ID no especificado");
            return;
        }

        Game game = games.get(gameId);

        String playerId = "player_" + session.getId();
        Player player = new Player(playerId, session.getId());

        if (game.addPlayer(player)) {
            sessionGameMap.put(session.getId(), gameId);

            sendMessage(session, createMessage("GAME_CREATED", Map.of(
                    "gameId", gameId,
                    "playerSymbol", player.getSymbol()
            )));

            // Enviar estado del juego a este jugador
            sendGameState(session, game);

            // Broadcast del nuevo estado a todos los jugadores
            broadcastGameState(gameId);

            System.out.println("Jugador " + playerId + " unido a: " + gameId);
        } else {
            sendErrorMessage(session, "No se pudo unir al juego");
        }
    }

    // Gestiona el movimiento del jugador
    private void handleMakeMove(WebSocketSession session, Map<String, Object> data) throws IOException {
        Integer position = (Integer) data.get("position");

        if (position == null) {
            sendErrorMessage(session, "Posición no especificada");
            return;
        }

        String gameId = sessionGameMap.get(session.getId());

        Game game = games.get(gameId);
        if (game == null) {
            sendErrorMessage(session, "Juego no encontrado");
            return;
        }

        String playerId = "player_" + session.getId();

        if (position < 0 || position > 8) {
            sendErrorMessage(session, "Posición inválida: " + position);
            return;
        }

        if (game.makeMove(playerId, position)) {
            broadcastGameState(gameId);
            System.out.println("Movimiento realizado: jugador " + playerId + " en posición " + position);
        } else {
            sendErrorMessage(session, "Movimiento inválido");
        }
    }

    private void handleResetGame(WebSocketSession session) throws IOException {
        String gameId = sessionGameMap.get(session.getId());
        if (gameId != null) {
            Game game = games.get(gameId);
            if (game != null) {
                game.reset();
                broadcastGameState(gameId);
                System.out.println("Juego reiniciado: " + gameId);
            }
        }
    }

    private void sendGameState(WebSocketSession session, Game game) throws IOException {
        Map<String, Object> gameState = createGameState(game);
        sendMessage(session, gameState);
    }

    private void broadcastGameState(String gameId) throws IOException {
        Game game = games.get(gameId);
        if (game == null) return;

        Map<String, Object> gameState = createGameState(game);

        // Enviar a cada sesión de forma secuencial
        for (Map.Entry<String, String> entry : sessionGameMap.entrySet()) {
            if (gameId.equals(entry.getValue())) {
                WebSocketSession session = sessions.get(entry.getKey());
                if (session != null && session.isOpen()) {
                    try {
                        sendMessage(session, gameState);
                    } catch (IOException e) {
                        System.err.println("Error enviando a sesión " + session.getId() + ": " + e.getMessage());
                    }
                }
            }
        }
    }

    private Map<String, Object> createGameState(Game game) {
        Map<String, Object> state = new HashMap<>();
        state.put("type", "GAME_STATE");
        state.put("board", game.getBoard() != null ? game.getBoard() : new String[9]);
        state.put("currentPlayer", game.getCurrentPlayer() != null ? game.getCurrentPlayer() : "X");
        state.put("gameOver", game.isGameOver());
        state.put("playersCount", game.getPlayerCount());

        // Manejar winner que puede ser null
        String winner = game.getWinner();
        if (winner != null) {
            state.put("winner", winner);
        } else {
            state.put("winner", ""); // String vacío en lugar de null
        }

        return state;
    }

    private Map<String, Object> createMessage(String type, Map<String, Object> data) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", type);
        if (data != null) {
            message.putAll(data);
        }
        return message;
    }

    private void sendErrorMessage(WebSocketSession session, String errorMessage) throws IOException {
        sendMessage(session, createMessage("ERROR", Map.of("message", errorMessage)));
    }

    private synchronized void sendMessage(WebSocketSession session, Object message) throws IOException {
        if (session != null && session.isOpen()) {
            try {
                String messageJson = mapper.writeValueAsString(message);
                synchronized (session) {
                    session.sendMessage(new TextMessage(messageJson));
                }
                System.out.println("Mensaje enviado a " + session.getId() + ": " + messageJson);
            } catch (IllegalStateException e) {
                System.err.println("Sesión no está en estado válido: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error enviando mensaje: " + e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("Error de transporte para sesión " + session.getId() + ": " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String gameId = sessionGameMap.remove(session.getId());
        sessions.remove(session.getId());

        if (gameId != null) {
            Game game = games.get(gameId);
            if (game != null) {
                String playerId = "player_" + session.getId();
                game.getPlayers().remove(playerId);

                // Notificar a los jugadores restantes
                if (game.getPlayerCount() > 0) {
                    broadcastGameState(gameId);
                } else {
                    games.remove(gameId);
                    System.out.println("Juego eliminado: " + gameId);
                }
            }
        }

        System.out.println("Conexión cerrada: " + session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
