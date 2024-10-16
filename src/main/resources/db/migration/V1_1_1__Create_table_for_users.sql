CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS USERS
(
    ID         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    USERNAME   VARCHAR(255) UNIQUE NOT NULL,
    PASSWORD   VARCHAR(255) NOT NULL
    );

INSERT INTO USERS (USERNAME, PASSWORD) VALUES ('admin', '$2b$12$s4WgrlVgjHpLwJTW32RUDu8PI2.uOC5F8yP.LiTQoAZjq7gBYonzS');
INSERT INTO USERS (USERNAME, PASSWORD) VALUES ('user', '$2b$12$jxQvWXYimYOlMn0LK.t/nOpjKfa0XbkCLUxf/gTvDSIsfwSA6kiqm');
