// js/components/Square.js
function Square({ value, onSquareClick, isWinner }) {
    const className = `square ${isWinner ? 'winner' : ''}`;

    return (
        <button
            className={className}
            onClick={onSquareClick}
        >
            {value}
        </button>
    );
}