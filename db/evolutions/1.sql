# Update question_type
# Jobs schema
 
# --- !Ups
UPDATE question_type
SET supported=1
WHERE type_name='geopoint';

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
UPDATE question_type
SET supported=0
WHERE type_name='geopoint';

DROP TABLE jobs;


