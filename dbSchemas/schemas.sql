-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: 140.134.26.71    Database: release-1-0-2
-- ------------------------------------------------------
-- Server version	5.7.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `EatTask`
--

DROP TABLE IF EXISTS `EatTask`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EatTask` (
  `TaskID` int(11) NOT NULL AUTO_INCREMENT,
  `StoreAddress` varchar(1000) NOT NULL,
  `Destination` varchar(1000) NOT NULL,
  PRIMARY KEY (`TaskID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EatTask`
--

LOCK TABLES `EatTask` WRITE;
/*!40000 ALTER TABLE `EatTask` DISABLE KEYS */;
/*!40000 ALTER TABLE `EatTask` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HouseworkTask`
--

DROP TABLE IF EXISTS `HouseworkTask`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HouseworkTask` (
  `TaskID` int(11) NOT NULL AUTO_INCREMENT,
  `StartTime` datetime NOT NULL,
  `EndTime` datetime NOT NULL,
  `Location` varchar(1000) NOT NULL,
  PRIMARY KEY (`TaskID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HouseworkTask`
--

LOCK TABLES `HouseworkTask` WRITE;
/*!40000 ALTER TABLE `HouseworkTask` DISABLE KEYS */;
/*!40000 ALTER TABLE `HouseworkTask` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Message`
--

DROP TABLE IF EXISTS `Message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(45) DEFAULT NULL,
  `userID` int(11) NOT NULL,
  `receiverID` int(11) NOT NULL,
  `postTime` datetime DEFAULT NULL,
  `taskID` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `u_fk_idx` (`userID`),
  KEY `reciever_fk_idx` (`receiverID`),
  KEY `task_fk_idx` (`taskID`),
  CONSTRAINT `receiver_fk` FOREIGN KEY (`receiverID`) REFERENCES `userData` (`userID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `task_fk` FOREIGN KEY (`taskID`) REFERENCES `Task` (`TaskID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_fk` FOREIGN KEY (`userID`) REFERENCES `userData` (`userID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Message`
--

LOCK TABLES `Message` WRITE;
/*!40000 ALTER TABLE `Message` DISABLE KEYS */;
INSERT INTO `Message` VALUES (2,'testing321',1,1,'2020-10-08 10:55:33',200),(3,'testing321',1,1,'2020-10-08 10:55:38',200),(4,'testing321',1,1,'2020-10-08 10:58:10',200),(5,'testing321',1,1,'2020-10-08 10:58:16',200),(6,'testing321',1,1,'2020-10-08 10:58:20',200),(7,'testing321',1,1,'2020-10-08 10:58:24',200),(8,'testing321',1,1,'2020-10-08 11:06:31',200),(9,'franky',1,1,'2020-10-07 12:15:00',200);
/*!40000 ALTER TABLE `Message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Task`
--

DROP TABLE IF EXISTS `Task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Task` (
  `TaskID` int(11) NOT NULL AUTO_INCREMENT,
  `TaskName` varchar(255) NOT NULL,
  `Message` varchar(2000) DEFAULT NULL,
  `StartPostTime` datetime NOT NULL,
  `EndPostTime` datetime DEFAULT NULL,
  `Salary` int(11) NOT NULL,
  `TypeName` varchar(45) DEFAULT NULL,
  `ReleaseUserID` int(11) DEFAULT NULL,
  `ReleaseTime` datetime DEFAULT NULL,
  `ReceiveUserID` int(11) DEFAULT NULL,
  `ReceiveTime` datetime DEFAULT NULL,
  `TaskAddress` varchar(255) DEFAULT NULL,
  `TaskCity` int(11) DEFAULT NULL,
  PRIMARY KEY (`TaskID`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Task`
--

LOCK TABLES `Task` WRITE;
/*!40000 ALTER TABLE `Task` DISABLE KEYS */;
INSERT INTO `Task` VALUES (200,'中文','testing-message','2020-10-08 09:55:15','2020-10-08 09:55:15',500,'EatTask',20,'2020-10-08 09:55:15',20,'2020-10-08 09:55:15','testing_Address',1);
/*!40000 ALTER TABLE `Task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TaskState`
--

DROP TABLE IF EXISTS `TaskState`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TaskState` (
  `TaskStateID` int(11) NOT NULL AUTO_INCREMENT,
  `TaskState` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`TaskStateID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TaskState`
--

LOCK TABLES `TaskState` WRITE;
/*!40000 ALTER TABLE `TaskState` DISABLE KEYS */;
/*!40000 ALTER TABLE `TaskState` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Task_TaskState`
--

DROP TABLE IF EXISTS `Task_TaskState`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Task_TaskState` (
  `Task_TaskStateID` int(11) NOT NULL,
  `TaskID` int(11) DEFAULT NULL,
  `TaskStateID` int(11) DEFAULT NULL,
  `StepNumber` datetime DEFAULT NULL,
  `FinishTime` datetime DEFAULT NULL,
  PRIMARY KEY (`Task_TaskStateID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Task_TaskState`
--

LOCK TABLES `Task_TaskState` WRITE;
/*!40000 ALTER TABLE `Task_TaskState` DISABLE KEYS */;
/*!40000 ALTER TABLE `Task_TaskState` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userComment`
--

DROP TABLE IF EXISTS `userComment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userComment` (
  `userID` int(11) NOT NULL,
  `comment` varchar(45) DEFAULT NULL,
  `reviewerID` int(11) NOT NULL,
  `rank` int(11) NOT NULL,
  `commentTime` datetime NOT NULL,
  PRIMARY KEY (`userID`),
  CONSTRAINT `reviewerID` FOREIGN KEY (`userID`) REFERENCES `userData` (`userID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userID` FOREIGN KEY (`userID`) REFERENCES `userData` (`userID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userComment`
--

LOCK TABLES `userComment` WRITE;
/*!40000 ALTER TABLE `userComment` DISABLE KEYS */;
/*!40000 ALTER TABLE `userComment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userData`
--

DROP TABLE IF EXISTS `userData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userData` (
  `userID` int(255) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) DEFAULT NULL,
  `userPhone` varchar(255) DEFAULT NULL,
  `userPassword` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userData`
--

LOCK TABLES `userData` WRITE;
/*!40000 ALTER TABLE `userData` DISABLE KEYS */;
INSERT INTO `userData` VALUES (1,'franky','0912345678','12341234');
/*!40000 ALTER TABLE `userData` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-08 11:34:17
