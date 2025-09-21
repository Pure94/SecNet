document.addEventListener('DOMContentLoaded', (event) => {
    const toggleConsoleButton = document.getElementById('toggleConsoleButton');
    const consoleContainer = document.getElementById('consoleContainer');
    const consoleInput = document.getElementById('consoleInput');
    const consoleOutput = document.getElementById('consoleOutput');

    toggleConsoleButton.addEventListener('click', () => {
        consoleContainer.classList.toggle('revealed');
        if (consoleContainer.classList.contains('revealed')) {
            consoleInput.focus();
        }
    });

    consoleInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            e.preventDefault();
            const command = consoleInput.value.trim();

            if (command) {
                consoleOutput.innerHTML += `\n> ${command}\n`;
                consoleOutput.innerHTML += `SYSTEM: ${Math.floor(Math.random() * 99999)} ERROR. Nieautoryzowany dostęp. Żądanie odrzucone.\n`;
                consoleInput.value = '';
                consoleOutput.scrollTop = consoleOutput.scrollHeight;
            }
        }
    });
});