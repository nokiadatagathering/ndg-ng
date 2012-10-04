/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `ndg`
--

USE `ndg2`;

--
-- Dumping data for table `company`
--

INSERT INTO `company` (`company_country`, `company_industry`, `company_name`, `company_size`, `company_type`) VALUES ('CompanyName','','CompanyCountry','CompanyIndustry','CompanySize');

--
-- Dumping data for table `ndg_role`
--

INSERT INTO `ndg_role` (`role_name`) VALUES ('Admin'),('Operator'),('Field Worker');

--
-- Dumping data for table `ndg_group`
--

INSERT INTO `ndg_group` (`group_name`, `ndg_user_id`) VALUES ('Demo',1);

--
-- Dumping data for table `ndg_user`
--

INSERT INTO `ndg_user` (`area_code`, `country_code`, `email`, `email_preferences`, `first_name`,`has_full_permissions`,`last_name`, `password`, `phone_number`, `user_admin`, `user_validated`, `username`, `validation_key`, `company_id`, `ndg_group_id` ) VALUES ('areaCode','countryCode','admin@admin.com','Y','firstName','Y','lastName','3d7b6783e2b63d8f462823c41f4d029d','phoneNumber','admin','Y','admin',NULL,1,1);

--
-- Dumping data for table `question`
--

INSERT INTO `question` ( `constraint_text`, `hint`, `label`, `object_name`, `readonly`, `required`, `default_answer_id`, `question_type_id`,`category_id`) VALUES (NULL,NULL,'What is your name ?','TextQuestion',0,0,NULL,1,1),(NULL,NULL,'On average, how often do you laugh per day ?','IntegerQuestion',0,0,NULL,2,1),(NULL,NULL,'Are you a thinker, a talker or a doer ?','ExclusiveQuestion',0,0,NULL,10,1),(NULL,NULL,'Which continents have you visited ?','MultipleQuestion',0,0,NULL,11,2),(NULL,NULL,'What do you like most about Finland?','TextQuestion2',0,0,NULL,1,2),(NULL,NULL,'Date','DateQuestion',0,0,NULL,4,2);

--
-- Dumping data for table `question_option`
--

INSERT INTO `question_option` (`label`, `option_index`, `option_value`, `question_id`) VALUES ('Doer',1,'doer',3),('Thinker',2,'thinker',3),('Talker',3,'talker',3),('Asia',1,'asia',4),('Africe',2,'africa',4),('Australia',3,'australia',4),('Europe',4,'europe',4),('Antarctica',5,'antarctica',4),('North America',6,'north',4),('Latin America',7,'latin',4);

--
-- Dumping data for table `question_type`
--

INSERT INTO `question_type` ( `supported`, `type_name`) VALUES (1,'string'),(1,'int'),(1,'decimal'),(1,'date'),(1,'geopoint'),(1,'binary#image'),(0,'binary#audio'),(0,'binary#video'),(0,'barcode'),(1,'select1'),(1,'select'),(1,'time');

--
-- Dumping data for table `survey`
--

INSERT INTO `category` (`category_index`, `label`, `object_name`, `survey_id`) VALUES (1,'Default','category1',1), (2,'Other','category2',1);

INSERT INTO `survey` (`survey_id`, `available`, `lang`, `title`, `upload_date`, `ndg_user_id`) VALUES ('1263929563',1,'eng','Demo Survey','2011-07-25 10:10:00',1);

--
-- Dumping data for table `transactionlog`
--


--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` (`username`, `ndg_role_role_name`) VALUES ('admin','Admin');

--
-- Dumping data for table `userbalance`
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-08-09 11:38:38
