-- src/main/resources/db/changelog/sql/03-create-conversations-table.sql

-- Tabela conversations
CREATE TABLE conversations (
                               id UUID PRIMARY KEY,
                               user_id UUID NOT NULL,
                               channel_id UUID NOT NULL,
                               user_message TEXT NOT NULL,
                               ai_response TEXT NOT NULL,
                               created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_conversations_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               CONSTRAINT fk_conversations_channel FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE
);