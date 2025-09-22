document.addEventListener('DOMContentLoaded', () => {
    // Pobranie danych JSON z ukrytych bloków script
    const channelsData = JSON.parse(document.getElementById('channels-data').textContent);
    const allUsersData = JSON.parse(document.getElementById('users-data').textContent);

    // Toast logic
    const errorToast = document.getElementById('error-toast');
    if (errorToast) {
        errorToast.style.display = 'block';
        setTimeout(() => {
            errorToast.style.display = 'none';
        }, 5000);
    }

    // Logika dla edycji system prompt
    const editModalOverlay = document.getElementById('edit-modal-overlay');
    const systemPromptTextarea = document.getElementById('edit-system-prompt');
    const channelIdInput = document.getElementById('edit-channel-id');
    const editPromptBtns = document.querySelectorAll('.edit-prompt-btn');
    const cancelEditBtn = document.getElementById('cancel-edit-btn');
    const csrfToken = document.querySelector('input[name="_csrf"]').value;

    editPromptBtns.forEach(button => {
        button.addEventListener('click', () => {
            const channelId = button.getAttribute('data-channel-id');
            const channel = channelsData.find(c => c.id.toString() === channelId.toString());

            if (channel) {
                channelIdInput.value = channel.id;
                systemPromptTextarea.value = channel.systemPrompt;
                editModalOverlay.style.display = 'flex';
            }
        });
    });

    cancelEditBtn.addEventListener('click', () => {
        editModalOverlay.style.display = 'none';
    });

    editModalOverlay.addEventListener('click', (e) => {
        if (e.target === editModalOverlay) {
            editModalOverlay.style.display = 'none';
        }
    });

    // Logika dla zarządzania użytkownikami
    const manageUsersBtn = document.querySelectorAll('.manage-users-btn');
    const manageUsersModal = document.getElementById('manage-users-modal-overlay');
    const manageChannelIdInput = document.getElementById('manage-channel-id');
    const manageUsersListContainer = document.getElementById('users-list-container');
    const cancelManageBtn = document.getElementById('cancel-manage-btn');
    const channelNameInModal = document.getElementById('channel-name-in-modal');

    manageUsersBtn.forEach(button => {
        button.addEventListener('click', () => {
            const channelId = button.getAttribute('data-channel-id');
            const channel = channelsData.find(c => c.id.toString() === channelId.toString());

            if (channel) {
                manageChannelIdInput.value = channel.id;
                channelNameInModal.textContent = channel.name;

                manageUsersListContainer.innerHTML = '';

                allUsersData.forEach(user => {
                    const assignedUsersInChannel = channel.userChannels || [];
                    const isAssigned = assignedUsersInChannel.some(au => au.user.uuid.toString() === user.uuid.toString());
                    const assignedUser = isAssigned ? assignedUsersInChannel.find(au => au.user.uuid.toString() === user.uuid.toString()) : null;
                    const limit = assignedUser ? assignedUser.remainingLimit : 100;

                    const userRow = document.createElement('div');
                    userRow.classList.add('user-row');
                    userRow.innerHTML = `
                        <label>
                            <input type="checkbox" name="user_${user.uuid}" ${isAssigned ? 'checked' : ''}>
                            <span>${user.username}</span>
                        </label>
                        <input type="number" name="limit_${user.uuid}" placeholder="Limit" min="0" value="${limit}" class="cyber-input" style="width: 80px;">
                        <form action="/admin-panel/update-user-limit" method="post" style="display: flex; align-items: center; gap: 5px;">
                            <input type="hidden" name="channelId" value="${channel.id}">
                            <input type="hidden" name="userId" value="${user.uuid}">
                            <input type="hidden" name="newLimit" class="limit-input" value="${limit}">
                            <input type="hidden" name="_csrf" value="${csrfToken}">
                            <button type="submit" class="cyber-button-small">Ustaw limit</button>
                        </form>
                        <form action="/admin-panel/reset-user-limit" method="post" style="display: flex; align-items: center; gap: 5px;">
                            <input type="hidden" name="channelId" value="${channel.id}">
                            <input type="hidden" name="userId" value="${user.uuid}">
                            <input type="hidden" name="_csrf" value="${csrfToken}">
                            <button type="submit" class="cyber-button-small" style="background-color: var(--neon-blue); box-shadow: 0 0 5px var(--neon-blue);">Resetuj</button>
                        </form>
                    `;
                    manageUsersListContainer.appendChild(userRow);
                });

                manageUsersModal.style.display = 'flex';
            }
        });
    });

    cancelManageBtn.addEventListener('click', () => {
        manageUsersModal.style.display = 'none';
    });

    manageUsersModal.addEventListener('click', (e) => {
        if (e.target === manageUsersModal) {
            manageUsersModal.style.display = 'none';
        }
    });

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            editModalOverlay.style.display = 'none';
            manageUsersModal.style.display = 'none';
        }
    });

    manageUsersListContainer.addEventListener('input', (e) => {
        if (e.target.type === 'number') {
            // Find the closest parent user-row
            const userRow = e.target.closest('.user-row');
            if (userRow) {
                // Find the specific form for updating the limit within that row
                const updateForm = userRow.querySelector('form[action="/admin-panel/update-user-limit"]');
                if (updateForm) {
                    // Update the hidden input in that form
                    updateForm.querySelector('.limit-input').value = e.target.value;
                }
            }
        }
    });
});