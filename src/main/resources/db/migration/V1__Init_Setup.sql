CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL, -- bcrypt hash
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20)
);

CREATE TABLE role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Insert default roles
INSERT INTO role (name) VALUES ('ROLE_USER');
INSERT INTO role (name) VALUES ('ROLE_ADMIN');

-- Insert default user 'guest' with password 'guest'
INSERT INTO users (username, password, first_name, last_name, email, phone)
VALUES (
    'guest',
    '$2a$10$NggnZLLtr8.NBShHJ5HUeu.jZspZZ.30iTSW193hApXJ93UQqW2/O',
    'Guest',
    'User',
    'guest@example.com',
    NULL
);

-- Assign ROLE_USER to guest user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id 
FROM users u, role r 
WHERE u.username = 'guest' AND r.name = 'ROLE_USER';
