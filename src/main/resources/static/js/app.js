// js/app.js
function App() {
    return (
        <div>
            <h1 style={{textAlign: 'center', color: '#333'}}>
                Tres en Raya - React
            </h1>
            <Game />
        </div>
    );
}

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<App />);