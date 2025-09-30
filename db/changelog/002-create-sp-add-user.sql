--liquibase formatted sql

--changeset dev:002-create-sp-add-user
CREATE OR REPLACE PROCEDURE sp_add_user (
    p_username   IN VARCHAR2,
    p_email      IN VARCHAR2
) AS
BEGIN
INSERT INTO users (username, email, created_at)
VALUES (p_username, p_email, SYSDATE);
END;
/
--rollback DROP PROCEDURE sp_add_user;
