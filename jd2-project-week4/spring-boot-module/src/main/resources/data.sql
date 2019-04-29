INSERT INTO role (name) VALUES ('CUSTOMER');
INSERT INTO role (name) VALUES ('ADMIN');
INSERT INTO user (username, password, role_id) VALUES ('admin@admin', '$2a$10$yOiWMdbf/yaOxx8eqs9XtObXTsb7iy8VTQdZpANPUvdJ8yvYKtH7K', (SELECT r.id FROM role r WHERE r.name = 'ADMIN'));
INSERT INTO user (username, password, role_id) VALUES ('customer@customer', '$2a$10$yOiWMdbf/yaOxx8eqs9XtObXTsb7iy8VTQdZpANPUvdJ8yvYKtH7K', (SELECT r.id FROM role r WHERE r.name = 'CUSTOMER'));