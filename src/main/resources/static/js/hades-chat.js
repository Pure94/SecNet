document.addEventListener('DOMContentLoaded', () => {
    const chatForm = document.getElementById('chatForm');
    const chatInput = document.getElementById('chatInput');
    const chatOutput = document.getElementById('chatOutput');
    const csrfToken = document.getElementById('csrfToken').value;

    function appendToChat(text, isUser = false) {
        const div = document.createElement('div');
        div.classList.add('log-line');
        if (isUser) {
            div.textContent = `> ${text}`;
            div.classList.add('log-system');
        } else {
            div.textContent = `HADES: ${text}`;
            div.classList.add('log-hades');
        }
        chatOutput.appendChild(div);
        chatOutput.scrollTop = chatOutput.scrollHeight;
    }

    chatForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const message = chatInput.value.trim();
        if (message === "") return;

        appendToChat(message, true);
        chatInput.value = '';

        const requestData = {
            username: "rgrzybicki",
            channelName: "hades_deep_network",
            message: message
        };

        fetch('/api/send-message', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            },
            body: JSON.stringify(requestData)
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(errorData => Promise.reject(errorData));
                }
                return response.json();
            })
            .then(data => {
                appendToChat(data.response, false);
            })
            .catch(error => {
                appendToChat(`ERROR: Błąd komunikacji z HADES. Kod błędu: ${error.message}`, false);
                console.error('Błąd:', error);
            });
    });
});