function Board({ squares, onPlay, currentPlayer, mySymbol, gameOver }) {
    const handleClick = (i) => {
        if (!gameOver && !squares[i] && currentPlayer === mySymbol) {
            onPlay(i);
        }
    };

    return (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 64px)', gap: '2px' }}>
            {squares.map((square, i) => (
                <Square
                    key={i}
                    value={square}
                    onSquareClick={() => handleClick(i)}
                    disabled={gameOver || square !== null || currentPlayer !== mySymbol}
                />
            ))}
        </div>
    );
}