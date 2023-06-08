DROP TABLE IF EXISTS users, items;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  available BOOLEAN,
  owner BIGINT,
  request VARCHAR(1000),
  CONSTRAINT pk_item PRIMARY KEY (id)
  --CONSTRAINT UQ_ITEM_NAME UNIQUE (name)
);

