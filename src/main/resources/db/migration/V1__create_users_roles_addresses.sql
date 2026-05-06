-- V1__create_users_roles_addresses.sql
-- PostgreSQL syntax

-- ─────────────────────────────────────────
-- TABLE: roles
-- ─────────────────────────────────────────
CREATE TABLE roles (
    id      BIGSERIAL       NOT NULL,   -- PostgreSQL auto increment (not AUTO_INCREMENT)
    name    VARCHAR(50)     NOT NULL,

    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uq_roles_name UNIQUE (name)
);

INSERT INTO roles (name) VALUES ('ROLE_CUSTOMER');
INSERT INTO roles (name) VALUES ('ROLE_VENDOR');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');


-- ─────────────────────────────────────────
-- TABLE: users
-- ─────────────────────────────────────────
CREATE TABLE users (
    id          BIGSERIAL       NOT NULL,
    full_name   VARCHAR(100)    NOT NULL,
    email       VARCHAR(100)    NOT NULL,
    password    VARCHAR(255)    NOT NULL,
    status      VARCHAR(20)     NOT NULL    DEFAULT 'ACTIVE',
    version     INT             NOT NULL    DEFAULT 0,
    created_at  TIMESTAMP       NOT NULL    DEFAULT NOW(),
    updated_at  TIMESTAMP       NOT NULL    DEFAULT NOW(),   -- PostgreSQL has no ON UPDATE

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_status CHECK (status IN ('ACTIVE', 'SUSPENDED', 'DELETED'))
);

CREATE INDEX idx_users_email ON users (email);


-- ─────────────────────────────────────────
-- TABLE: user_roles
-- ─────────────────────────────────────────
CREATE TABLE user_roles (
    user_id     BIGINT  NOT NULL,
    role_id     BIGINT  NOT NULL,

    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles (id)
        ON DELETE CASCADE
);


-- ─────────────────────────────────────────
-- TABLE: addresses
-- ─────────────────────────────────────────
CREATE TABLE addresses (
    id              BIGSERIAL       NOT NULL,
    user_id         BIGINT          NOT NULL,
    address_line1   VARCHAR(255)    NOT NULL,
    address_line2   VARCHAR(255),
    city            VARCHAR(100)    NOT NULL,
    state           VARCHAR(100)    NOT NULL,
    postal_code     VARCHAR(20)     NOT NULL,
    country         VARCHAR(100)    NOT NULL    DEFAULT 'Australia',
    is_default      BOOLEAN         NOT NULL    DEFAULT FALSE,

    CONSTRAINT pk_addresses PRIMARY KEY (id),

    CONSTRAINT fk_addresses_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_addresses_user_id ON addresses (user_id);