/*****************************************************
 ** This file is 100% ***GENERATED***, DO NOT EDIT! **
 *****************************************************/

DROP USER IF EXISTS 'userdb';
DROP DATABASE IF EXISTS userdb;

CREATE DATABASE userdb DEFAULT CHARACTER SET 'UTF8' DEFAULT COLLATE utf8_unicode_ci;

CREATE USER 'userdb' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'userdb' WITH GRANT OPTION;
ALTER USER 'userdb' IDENTIFIED WITH mysql_native_password BY 'password';

USE userdb;

CREATE TABLE userdb.user (
  user_id VARCHAR(64) NOT NULL,
  user_name VARCHAR(64),
  password VARCHAR(64)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE userdb.user ADD CONSTRAINT user_pk PRIMARY KEY(user_id);
