// js/components/Board.js
function Board({ xIsNext, squares, onPlay, winningLine }) {
    function handleClick(i) {
        if (calculateWinner(squares) || squares[i]) {
            return;
        }

        const nextSquares = squares.slice();
        nextSquares[i] = xIsNext ? 'X' : 'O';
        onPlay(nextSquares);
    }

    const winnerInfo = calculateWinner(squares);
    const winner = winnerInfo ? winnerInfo.winner : null;
    const winnerLine = winnerInfo ? winnerInfo.line : [];

    let status;
    if (winner) {
        status = 'Ganador: ' + winner;
    } else if (squares.every(square => square !== null)) {
        status = 'Empate!';
    } else {
        status = 'Siguiente jugador: ' + (xIsNext ? 'X' : 'O');
    }

    // Crear el tablero
    const boardRows = [];
    for (let row = 0; row < 3; row++) {
        const boardRow = [];
        for (let col = 0; col < 3; col++) {
            const index = row * 3 + col;
            boardRow.push(
                <Square
                    key={index}
                    value={squares[index]}
                    onSquareClick={() => handleClick(index)}
                    isWinner={winnerLine.includes(index)}
                />
            );
        }
        boardRows.push(
            <div key={row} className="board-row">
                {boardRow}
            </div>
        );
    }

    return (
        <div>
            <div className="status">{status}</div>
            {boardRows}
        </div>
    );
}

// Función para calcular el ganador
function calculateWinner(squares) {
    const lines = [
        [0, 1, 2],
        [3, 4, 5],
        [6, 7, 8],
        [0, 3, 6],
        [1, 4, 7],
        [2, 5, 8],
        [0, 4, 8],
        [2, 4, 6],
    ];

    for (let i = 0; i < lines.length; i++) {
        const [a, b, c] = lines[i];
        if (squares[a] && squares[a] === squares[b] && squares[a] === squares[c]) {
            return {
                winner: squares[a],
                line: [a, b, c]
            };
        }
    }
    return null;
}