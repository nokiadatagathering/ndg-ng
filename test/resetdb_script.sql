DROP schema IF EXISTS `ndg`;
CREATE schema  IF NOT EXISTS `ndg`;
USE `ndg`;
GRANT USAGE ON *.* to 'ndg'@'%' identified by 'ndg';
GRANT ALL PRIVILEGES ON *.* to 'ndg'@'%';