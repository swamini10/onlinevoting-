
CREATE TABLE online_voting.role (
   id BIGINT NOT NULL AUTO_INCREMENT,
   name VARCHAR(50) UNIQUE NOT NULL,
   is_active BIT(1) DEFAULT 1,
   description VARCHAR(255),
   PRIMARY KEY (id)
);
