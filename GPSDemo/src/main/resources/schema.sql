CREATE TABLE METADATA(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    description VARCHAR(200000) DEFAULT NULL,
    author VARCHAR(250) DEFAULT NULL,
    time VARCHAR(250) DEFAULT NULL
);