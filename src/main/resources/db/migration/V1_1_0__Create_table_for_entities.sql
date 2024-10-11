CREATE TABLE IF NOT EXISTS ATTACHMENT
(
    ID         UUID PRIMARY KEY,
    FILE_NAME  VARCHAR(255) NOT NULL,
    CREATED_AT TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- CREATE TABLE IF NOT EXISTS VIDEO
-- (
--     ID         UUID PRIMARY KEY,
--     FILE_NAME  VARCHAR(255) UNIQUE NOT NULL,
--     CREATED_AT TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
-- );
--
-- CREATE TABLE IF NOT EXISTS FINGERPRINT
-- (
--     ID                   UUID PRIMARY KEY,
--     FILE_NAME            VARCHAR(255) UNIQUE NOT NULL,
--     FINGERPRINT_POSITION SMALLINT            NOT NULL,
--     CREATED_AT           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
-- );
--
--
-- CREATE TABLE IF NOT EXISTS FACE
-- (
--     ID         UUID PRIMARY KEY,
--     FILE_NAME  VARCHAR(255) UNIQUE NOT NULL,
--     CREATED_AT TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
-- );