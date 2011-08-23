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

USE `ndg`;

--
-- Dumping data for table `company`
--

INSERT INTO `company` (`companyId`, `companyCountry`, `companyIndustry`, `companyName`, `companySize`, `companyType`) VALUES (1,'CompanyName','','CompanyCountry','CompanyIndustry','CompanySize');

--
-- Dumping data for table `ndg_role`
--

INSERT INTO `ndg_role` (`ndgRoleId`, `roleName`) VALUES (1,'Admin'),(2,'Operator'),(3,'Field Worker');

--
-- Dumping data for table `ndg_user`
--

INSERT INTO `ndg_user` (`ndgUserId`, `areaCode`, `countryCode`, `editorSettings`, `email`, `emailPreferences`, `firstName`, `firstTimeUse`, `hasFullPermissions`, `howDoYouPlanUseNdg`, `lastName`, `password`, `phoneNumber`, `userAdmin`, `userValidated`, `username`, `validationKey`, `whoUseIt`, `companyId`) VALUES (1,'areaCode','countryCode',NULL,'admin@admin.com','Y','firstName','N','Y',NULL,'lastName','b235fd01d8130026cfcca86a1b206208','phoneNumber','Y','Y','admin',NULL,'Y',1);

--
-- Dumping data for table `question`
--

INSERT INTO `question` (`questionId`, `constraintText`, `hint`, `label`, `objectName`, `readonly`, `required`, `defaultAnswerDefaultAnswerId`, `questionTypeQuestionTypeId`, `surveysSurveyId`) VALUES (1,NULL,NULL,'What is your name ?','TextQuestion',0,0,NULL,1,'1263929563'),(2,NULL,NULL,'On average, how often do you laugh per day ?','IntegerQuestion',0,0,NULL,2,'1263929563'),(3,NULL,NULL,'Are you a thinker, a talker or a doer ?','ExclusiveQuestion',0,0,NULL,10,'1263929563'),(4,NULL,NULL,'Which continents have you visited ?','MultipleQuestion',0,0,NULL,11,'1263929563'),(5,NULL,NULL,'What do you like most about Finland?','TextQuestion2',0,0,NULL,1,'1263929563'),(6,NULL,NULL,'Date','DateQuestion',0,0,NULL,4,'1263929563');

--
-- Dumping data for table `question_option`
--

INSERT INTO `question_option` (`questionOptionId`, `label`, `optionIndex`, `optionValue`, `questionQuestionId`) VALUES (1,'Doer',1,'doer',3),(2,'Thinker',2,'thinker',3),(3,'Talker',3,'talker',3),(4,'Asia',1,'asia',4),(5,'Africe',2,'africa',4),(6,'Australia',3,'australia',4),(7,'Europe',4,'europe',4),(8,'Antarctica',5,'antarctica',4),(9,'North America',6,'north',4),(10,'Latin America',7,'latin',4);

--
-- Dumping data for table `question_type`
--

INSERT INTO `question_type` (`questionTypeId`, `supported`, `typeName`) VALUES (1,1,'string'),(2,1,'int'),(3,1,'decimal'),(4,1,'date'),(5,0,'geopoint'),(6,1,'binary#image'),(7,0,'binary#audio'),(8,0,'binary#video'),(9,0,'barcode'),(10,1,'select1'),(11,1,'select');

--
-- Dumping data for table `survey`
--

INSERT INTO `survey` (`surveyId`, `available`, `lang`, `title`, `uploadDate`, `ndgUserNdgUserId`) VALUES ('1263929563',1,'eng','Demo Survey','2011-07-25 10:10:00',1);

--
-- Dumping data for table `transactionlog`
--


--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` (`id`, `roleName`, `username`) VALUES (1,'Admin','admin');

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