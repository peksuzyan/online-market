# Accessible by root

CREATE DATABASE MARKET_PROD
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

CREATE DATABASE MARKET_TEST
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

CREATE USER 'market-admin'@'localhost'
  IDENTIFIED BY 'market-admin123';

GRANT ALL ON MARKET_PROD.* TO 'market-admin'@'localhost';
GRANT ALL ON MARKET_TEST.* TO 'market-admin'@'localhost';

CREATE USER 'market-user'@'localhost'
  IDENTIFIED BY 'market-user123';

GRANT SELECT, INSERT, UPDATE, DELETE ON MARKET_PROD.* TO 'market-user'@'localhost';

CREATE USER 'market-test'@'localhost'
  IDENTIFIED BY 'market-test123';

GRANT SELECT, INSERT, UPDATE, DELETE ON MARKET_TEST.* TO 'market-test'@'localhost';