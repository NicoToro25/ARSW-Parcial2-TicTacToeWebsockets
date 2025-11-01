class GameClient {
    constructor() {
        this.socket = null;
        this.handlers = new Map();
    }

    connect() {
        return new Promise((resolve, reject) => {
            const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
            const wsUrl = `${protocol}//${window.location.host}/ws/game`;

            this.socket = new WebSocket(wsUrl);

            this.socket.onopen = () => {
                console.log('Conectado al servidor');
                resolve();
            };

            this.socket.onmessage = (event) => {
                try {
                    const message = JSON.parse(event.data);
                    const handler = this.handlers.get(message.type);
                    if (handler) {
                        handler(message);
                    }
                } catch (error) {
                    console.error('Error parsing message:', error);
                }
            };

            this.socket.onerror = (error) => {
                console.error('WebSocket error:', error);
                reject(error);
            };

            this.socket.onclose = () => {
                console.log('Conexión cerrada');
            };
        });
    }

    on(messageType, handler) {
        this.handlers.set(messageType, handler);
    }

    send(message) {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
            this.socket.send(JSON.stringify(message));
        } else {
            console.error('WebSocket no conectado');
        }
    }

    createGame() {
        this.send({ type: 'CREATE_GAME' });
    }

    joinGame(gameId) {
        this.send({ type: 'JOIN_GAME', gameId: gameId });
    }

    makeMove(position) {
        this.send({ type: 'MAKE_MOVE', position: position });
    }

    resetGame() {
        this.send({ type: 'RESET_GAME' });
    }

    disconnect() {
        if (this.socket) {
            this.socket.close();
        }
    }
}

const gameClient = new GameClient();