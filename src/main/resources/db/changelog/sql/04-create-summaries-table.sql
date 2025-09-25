CREATE TABLE summaries (
    id BIGSERIAL PRIMARY KEY,
    channel_id UUID NOT NULL,
    user_id UUID NOT NULL,
    summary TEXT,
    created_at TIMESTAMP,
    CONSTRAINT fk_channel FOREIGN KEY (channel_id) REFERENCES channels(id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);
