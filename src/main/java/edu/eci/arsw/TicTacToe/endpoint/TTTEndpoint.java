package edu.eci.arsw.TicTacToe.endpoint;

import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

@Component
@ServerEndpoint("/tttService")
public class TTTEndpoint {

    private static final Logger logger =
            Logger.getLogger(TTTEndpoint.class.getName());

    static Queue<Session> queue = new ConcurrentLinkedQueue<>();

    Session ownSession = null;
}
