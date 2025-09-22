-- src/main/resources/db/changelog/sql/02-create-channels-and-user-channels-tables.sql

-- Tabela channels
CREATE TABLE channels (
                          id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          system_prompt TEXT
);

-- Tabela user_channels
CREATE TABLE user_channels (
                               user_id UUID,
                               channel_id UUID,
                               remaining_limit INTEGER,
                               CONSTRAINT pk_user_channels PRIMARY KEY (user_id, channel_id),
                               CONSTRAINT fk_user_channels_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               CONSTRAINT fk_user_channels_channel FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE
);