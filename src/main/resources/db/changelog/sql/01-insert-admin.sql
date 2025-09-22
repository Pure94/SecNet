-- src/main/resources/db/changelog/db/01-insert-admin-user.sql

INSERT INTO roles (id, name) VALUES (uuid_in(md5(random()::text || random()::text)::cstring), 'ADMIN');
INSERT INTO users (id, username, password) VALUES (uuid_in(md5(random()::text || random()::text)::cstring), 'admin', '$2a$10$Qmxf76An.TVwTCRKOhOlPe7baVbvISPNsHd198udgfuum3wfXHEOa');
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ADMIN';