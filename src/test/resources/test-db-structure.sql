CREATE TABLE IF NOT EXISTS employee (
     id          BIGINT AUTO_INCREMENT PRIMARY KEY,
     email       VARCHAR(32) UNIQUE NOT NULL,
     password    VARCHAR NOT NULL,
     first_name  VARCHAR(32) NOT NULL,
     last_name   VARCHAR(32) NOT NULL,
     salary      DECIMAL NOT NULL,
     birthday    DATE NOT NULL,
     role        VARCHAR(32) NOT NULL
)