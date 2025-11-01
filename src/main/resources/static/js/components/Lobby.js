function Lobby({ onJoinGame }) {
    const [gameId, setGameId] = React.useState('');
    const [isConnected, setIsConnected] = React.useState(false);

    React.useEffect(() => {
        // Conectar al WebSocket cuando el componente se monta
        gameClient.connect().then(() => {
            setIsConnected(true);
        }).catch(error => {
            console.error('Failed to connect:', error);
        });
    }, []);

    const handleCreateGame = () => {
        gameClient.on('GAME_CREATED', (message) => {
            onJoinGame(message.gameId);
        });
        gameClient.createGame();
    };

    const handleJoinGame = () => {
        if (gameId.trim()) {
            onJoinGame(gameId.trim());
        }
    };

    return (
        <div style={{ padding: '20px', textAlign: 'center' }}>
            <h1>Tic Tac Toe Multiplayer</h1>
            <p>Estado: {isConnected ? 'Conectado' : 'Desconectado'}</p>

            <div style={{ margin: '20px 0' }}>
                <button
                    onClick={handleCreateGame}
                    disabled={!isConnected}
                    style={{ padding: '10px 20px', fontSize: '16px' }}
                >
                    Crear Nuevo Juego
                </button>
            </div>

            <div>
                <h3>O unirse a juego existente:</h3>
                <input
                    type="text"
                    placeholder="ID juego"
                    value={gameId}
                    onChange={(e) => setGameId(e.target.value)}
                    style={{ padding: '8px', marginRight: '10px', width: '200px' }}
                />
                <button
                    onClick={handleJoinGame}
                    disabled={!isConnected || !gameId.trim()}
                >
                    Unirse
                </button>
            </div>
        </div>
    );
}