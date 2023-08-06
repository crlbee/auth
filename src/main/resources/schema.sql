CREATE TABLE users (
  name VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (name)
);

CREATE TABLE payment (
  id BIGINT NOT NULL,
   name VARCHAR(50) NOT NULL,
  amount BIGINT,
  date TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (name) REFERENCES users(name)

);