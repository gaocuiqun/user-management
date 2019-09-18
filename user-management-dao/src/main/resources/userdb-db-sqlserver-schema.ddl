-----------------------------------------------------
-- This file is 100% ***GENERATED***, DO NOT EDIT! --
-----------------------------------------------------

-- Using 'userdb' as database name, schema name, login name, user name and role name.
-- For Test/Demo purpose only.
-- Fine-tuning is need for production.

DROP DATABASE IF EXISTS [userdb]
GO

CREATE DATABASE [userdb] ON
  PRIMARY (
    NAME       = 'userdb_data',
    FILENAME   = '/var/opt/mssql/data/userdb_data.mdf',
    SIZE       = 10MB,
    MAXSIZE    = UNLIMITED,
    FILEGROWTH = 10MB
  )
  LOG ON (
    NAME       = 'userdb_log',
    FILENAME   = '/var/opt/mssql/data/userdb_log.ldf',
    SIZE       = 10MB,
    MAXSIZE    = UNLIMITED,
    FILEGROWTH = 10MB
  )
  COLLATE Chinese_PRC_CI_AS
GO

-- The following options is executed on database 'userdb'

USE [userdb]
GO

DROP SCHEMA IF EXISTS [userdb]
GO

CREATE SCHEMA [userdb] AUTHORIZATION dbo
GO

DROP USER IF EXISTS [userdb]
GO

-- DROP LOGIN IF EXISTS [userdb]
DROP LOGIN [userdb]
GO

-- Change it for production since password is sensitive data.
CREATE LOGIN [userdb] WITH PASSWORD='my-Secret-pw'
GO

CREATE USER [userdb] FOR LOGIN [userdb] WITH DEFAULT_SCHEMA=[userdb]
GO

DROP ROLE IF EXISTS [user_management]
GO

CREATE ROLE [user_management] AUTHORIZATION dbo
GO

ALTER ROLE [user_management] ADD MEMBER [userdb]
GO

-- ALTER ROLE [user_management] DROP MEMBER [userdb]
-- GO

GRANT SELECT, UPDATE, DELETE ON SCHEMA::[userdb] to [userdb]

CREATE TABLE [userdb].[user] (
  [user_id] VARCHAR(64) NOT NULL,
  [user_name] VARCHAR(64),
  [password] VARCHAR(64)
)
GO

ALTER TABLE [userdb].[user] WITH CHECK
ADD CONSTRAINT [user_pk] PRIMARY KEY ([user_id])
GO

PRINT "Finished."
GO
