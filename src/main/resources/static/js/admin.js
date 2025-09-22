document.addEventListener('DOMContentLoaded', () => {
    const channelsData = JSON.parse(document.getElementById('channels-data').textContent);
    const allUsersData = JSON.parse(document.getElementById('users-data').textContent);

    const editModalOverlay = document.getElementById('edit-modal-overlay');
    const systemPromptTextarea = document.getElementById('edit-system-prompt');
    const channelIdInput = document.getElementById('edit-channel-id');
    const editPromptBtns = document.querySelectorAll('.edit-prompt-btn');
    const cancelEditBtn = document.getElementById('cancel-edit-btn');

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
                    // Sprawdź, czy channel.userChannels istnieje i jest tablicą, zanim użyjesz .some()
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
});