function Game({ gameId, onLeaveGame }) {
    const [squares, setSquares] = React.useState(Array(9).fill(null));
    const [currentPlayer, setCurrentPlayer] = React.useState('X');
    const [winner, setWinner] = React.useState(null);
    const [gameOver, setGameOver] = React.useState(false);
    const [mySymbol, setMySymbol] = React.useState(null);
    const [playersCount, setPlayersCount] = React.useState(1);

    React.useEffect(() => {
        // Configurar handlers de WebSocket
        const handleGameState = (message) => {
            console.log('Game state received:', message);
            setSquares(message.board || Array(9).fill(null));
            setCurrentPlayer(message.currentPlayer || 'X');
            setWinner(message.winner);
            setGameOver(message.gameOver || false);
            setPlayersCount(message.playersCount || 1);
        };

        const handleGameCreated = (message) => {
            console.log('Game created:', message);
            setMySymbol(message.playerSymbol);
        };

        const handleError = (message) => {
            console.error('WebSocket error:', message);
            alert('Error: ' + message.message);
        };

        gameClient.on('GAME_STATE', handleGameState);
        gameClient.on('GAME_CREATED', handleGameCreated);
        gameClient.on('ERROR', handleError);

        // Limpiar handlers al desmontar
        return () => {
            gameClient.handlers.delete('GAME_STATE');
            gameClient.handlers.delete('GAME_CREATED');
            gameClient.handlers.delete('ERROR');
        };
    }, []);

    const handlePlay = (position) => {
        console.log('Making move at position:', position);
        gameClient.makeMove(position);
    };

    const handleReset = () => {
        gameClient.resetGame();
    };

    const handleLeave = () => {
        gameClient.disconnect();
        onLeaveGame();
    };

    let status;
    if (winner === 'TIE') {
        status = '¡Empate!';
    } else if (winner) {
        status = `¡Ganador: ${winner}!`;
    } else {
        status = `Turno: ${currentPlayer}`;
        if (currentPlayer === mySymbol) {
            status += ' (Tu turno)';
        } else {
            status += ' (Turno del oponente)';
        }
    }

    return (
        <div style={{ padding: '20px', border: '1px solid #ccc', margin: '10px' }}>
            <h2>Juego: {gameId}</h2>
            <p><strong>Tu símbolo:</strong> {mySymbol || 'Esperando...'} | <strong>Jugadores:</strong> {playersCount}/2</p>
            <div style={{ margin: '10px 0', fontWeight: 'bold' }}>{status}</div>

            <Board
                squares={squares}
                onPlay={handlePlay}
                currentPlayer={currentPlayer}
                mySymbol={mySymbol}
                gameOver={gameOver}
            />

            <div style={{ marginTop: '10px' }}>
                <button onClick={handleReset}>Reiniciar</button>
                <button onClick={handleLeave} style={{ marginLeft: '10px' }}>Salir</button>
            </div>
        </div>
    );
}