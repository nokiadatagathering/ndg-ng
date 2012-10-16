# Jobs schema
 
# --- !Ups
 
CREATE TABLE jobs (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    surveyId varchar(255) NOT NULL,
    dateTo varchar(255) NOT NULL,
    dateFrom varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    complete boolean NOT NULL,
    PRIMARY KEY (id)
);
 
# --- !Downs
 
DROP TABLE jobs;

