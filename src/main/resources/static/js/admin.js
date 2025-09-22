document.addEventListener('DOMContentLoaded', () => {
    // Logika dla edycji system prompt
    const editModalOverlay = document.getElementById('edit-modal-overlay');
    const systemPromptTextarea = document.getElementById('edit-system-prompt');
    const channelIdInput = document.getElementById('edit-channel-id');
    const editPromptBtns = document.querySelectorAll('.edit-prompt-btn');
    const cancelEditBtn = document.getElementById('cancel-edit-btn');

    editPromptBtns.forEach(button => {
        button.addEventListener('click', () => {
            const channelId = button.getAttribute('data-channel-id');
            const channel = channelsData.find(c => c.id === channelId);

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
            const channelName = button.getAttribute('data-channel-name');
            const channel = channelsData.find(c => c.id === channelId);

            if (channel) {
                manageChannelIdInput.value = channel.id;
                channelNameInModal.textContent = channel.name;

                manageUsersListContainer.innerHTML = '';

                allUsersData.forEach(user => {
                    const isAssigned = channel.userChannels.some(au => au.user.uuid === user.uuid);
                    const assignedUser = isAssigned ? channel.userChannels.find(au => au.user.uuid === user.uuid) : null;
                    const limit = assignedUser ? assignedUser.remainingLimit : 100;

                    const userRow = document.createElement('div');
                    userRow.classList.add('user-row');
                    userRow.innerHTML = `
                        <label>
                            <input type="checkbox" name="user_${user.uuid}" ${isAssigned ? 'checked' : ''}>
                            <span>${user.username}</span>
                        </label>
                        <input type="number" name="limit_${user.uuid}" placeholder="Limit" min="0" value="${limit}" class="cyber-input" style="width: 80px;">
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

    // Obsługa zamknięcia okien modalnych za pomocą klawisza 'Escape'
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            editModalOverlay.style.display = 'none';
            manageUsersModal.style.display = 'none';
        }
    });
});