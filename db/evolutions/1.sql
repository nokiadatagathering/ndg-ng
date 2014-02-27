# Update question_type
# Jobs schema
 
# --- !Ups
UPDATE question_type
SET supported=1
WHERE type_name='geopoint';

 
# --- !Downs
UPDATE question_type
SET supported=0
WHERE type_name='geopoint';



