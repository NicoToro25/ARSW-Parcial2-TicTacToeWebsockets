function App() {
    const [currentView, setCurrentView] = React.useState('lobby');
    const [currentGameId, setCurrentGameId] = React.useState(null);

    const handleJoinGame = (gameId) => {
        setCurrentGameId(gameId);
        setCurrentView('game');

        // Si no es una creación, unirse al juego
        if (currentView === 'lobby') {
            gameClient.joinGame(gameId);
        }
    };

    const handleLeaveGame = () => {
        setCurrentView('lobby');
        setCurrentGameId(null);
    };

    if (currentView === 'game' && currentGameId) {
        return <Game gameId={currentGameId} onLeaveGame={handleLeaveGame} />;
    }

    return <Lobby onJoinGame={handleJoinGame} />;
}

// Renderizar la aplicación
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<App />);