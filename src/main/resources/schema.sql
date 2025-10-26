/*CREATE TABLE IF NOT EXISTS chat_message (
    id SERIAL PRIMARY KEY,
    session_id VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    response TEXT,
    model VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);*/

CREATE TABLE IF NOT EXISTS student (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    gender VARCHAR(10) NOT NULL,
    city VARCHAR(50) NOT NULL,
    insertion_method VARCHAR(50)
);
