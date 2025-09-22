-- src/main/resources/db/changelog/sql/00-create-users-and-roles-tables.sql

-- Tabela users
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

-- Tabela roles
CREATE TABLE roles (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE
);

-- Tabela users_roles dla relacji wiele do wielu
CREATE TABLE users_roles (
                             user_id UUID,
                             role_id UUID,
                             CONSTRAINT fk_users_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
                             CONSTRAINT fk_users_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);