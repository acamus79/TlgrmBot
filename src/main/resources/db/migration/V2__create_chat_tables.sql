CREATE TABLE conversations (
    id VARCHAR(36) PRIMARY KEY,
    telegram_chat_id BIGINT NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    last_message_at TIMESTAMP NOT NULL
);

CREATE TABLE messages (
    id VARCHAR(36) PRIMARY KEY,
    conversation_id VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    direction VARCHAR(20) NOT NULL,
    sent_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_conversation FOREIGN KEY (conversation_id) REFERENCES conversations(id)
);

CREATE INDEX idx_messages_conversation_id ON messages(conversation_id);
