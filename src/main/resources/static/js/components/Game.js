// js/components/Game.js
function Game() {
    const [history, setHistory] = React.useState([Array(9).fill(null)]);
    const [currentMove, setCurrentMove] = React.useState(0);
    const [isAscending, setIsAscending] = React.useState(true);
    const xIsNext = currentMove % 2 === 0;
    const currentSquares = history[currentMove];

    function handlePlay(nextSquares) {
        const nextHistory = [...history.slice(0, currentMove + 1), nextSquares];
        setHistory(nextHistory);
        setCurrentMove(nextHistory.length - 1);
    }

    function jumpTo(nextMove) {
        setCurrentMove(nextMove);
    }

    function toggleSortOrder() {
        setIsAscending(!isAscending);
    }

    const moves = history.map((squares, move) => {
        let description;
        if (move > 0) {
            // Encontrar la posición del último movimiento
            const prevSquares = history[move - 1];
            let position = '';
            for (let i = 0; i < squares.length; i++) {
                if (squares[i] !== prevSquares[i]) {
                    const row = Math.floor(i / 3) + 1;
                    const col = (i % 3) + 1;
                    position = ` (${row}, ${col})`;
                    break;
                }
            }

            description = 'Ir al movimiento #' + move + position;
        } else {
            description = 'Ir al inicio del juego';
        }

        return (
            <li key={move}>
                {move === currentMove ? (
                    <span>Estás en el movimiento #{move}</span>
                ) : (
                    <button onClick={() => jumpTo(move)}>
                        {description}
                    </button>
                )}
            </li>
        );
    });

    const sortedMoves = isAscending ? moves : moves.slice().reverse();

    return (
        <div className="game">
            <div className="game-board">
                <Board
                    xIsNext={xIsNext}
                    squares={currentSquares}
                    onPlay={handlePlay}
                />
            </div>
            <div className="game-info">
                <div>
                    <button onClick={toggleSortOrder}>
                        Orden: {isAscending ? 'Ascendente' : 'Descendente'}
                    </button>
                </div>
                <ol>{sortedMoves}</ol>
            </div>
        </div>
    );
}