-- MySQL dump 10.13  Distrib 5.7.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: todo
-- ------------------------------------------------------
-- Server version	5.7.36-log

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
-- Table structure for table `todolist`
--
DROP DATABASE IF EXISTS `todo`;
CREATE DATABASE `todo`;
use `todo`;
DROP TABLE IF EXISTS `todolist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todolist` (
  `project` char(255) NOT NULL COMMENT '项目名称',
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '待办集主键，自增',
  `todoID` int(11) NOT NULL COMMENT 'todo中待办ID',
  `userID` char(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `todolist_todos_id_fk` (`todoID`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1 COMMENT='待办集';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todolist`
--

LOCK TABLES `todolist` WRITE;
/*!40000 ALTER TABLE `todolist` DISABLE KEYS */;
INSERT INTO `todolist` VALUES ('ullamco consequat Excepteur',17,23,'111'),('aute cillum dolor tempor labore',18,23,'111'),('sunt labore est',19,23,'111'),('ad sunt',20,23,'111'),('ullamco elit nulla amet veniam',21,22,'222'),('cupidatat',22,22,'222'),('test',27,24,'222'),('ipsum culpa',28,59,'111'),('est deserunt',29,59,'111'),('laboris',30,59,'111'),('laborum Lorem do',31,59,'111'),('quis laborum cillum',32,59,'111');
/*!40000 ALTER TABLE `todolist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `todos`
--

DROP TABLE IF EXISTS `todos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `todos` (
  `name` varchar(255) NOT NULL COMMENT '待办名称',
  `category` varchar(255) NOT NULL,
  `timecategory` varchar(255) NOT NULL,
  `endtime` varchar(255) DEFAULT '0000-00-00 00:00:00',
  `starttime` varchar(255) DEFAULT '0000-00-00 00:00:00',
  `detail` varchar(255) DEFAULT NULL,
  `userID` char(255) NOT NULL,
  `id` int(11) NOT NULL COMMENT '待办id，唯一',
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`,`userID`),
  KEY `ix_todos_user_id` (`userID`),
  KEY `ix_todos_todoType` (`category`),
  KEY `ix_todos_todoName` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `todos`
--

LOCK TABLES `todos` WRITE;
/*!40000 ALTER TABLE `todos` DISABLE KEYS */;
INSERT INTO `todos` VALUES ('edqh','group','normalclock','2001-11-24 01:32:43','2008-03-08 13:37:32',NULL,'222',22,0),('2333','group','noclock','0000-00-00 00:00:00','0000-00-00 00:00:00',NULL,'222',24,0),('test','single','noclock','0000-00-00 00:00:00','0000-00-00 00:00:00',NULL,'222',25,0),('anomqeeyn','group','normalclock','1999-04-09 12:47:27','1996-02-07 12:54:16',NULL,'111',59,1),('cncttytq','single','backclock','2015-02-11 08:15:23','0000-00-00 00:00:00',NULL,'222',63,0);
/*!40000 ALTER TABLE `todos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `password` char(255) NOT NULL COMMENT '用户密码',
  `name` char(255) DEFAULT NULL COMMENT '用户名',
  `description` char(255) DEFAULT NULL COMMENT '用户账号描述',
  `userID` char(255) NOT NULL COMMENT '用户账号',
  `phone` char(11) NOT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('111','111','111','111','12345678900'),('220','220','333','220','18665368833'),('222','222','222','222','18226756788'),('RIgFI','333','333','333','18666254088'),('2^@^*n','xiaoxiao','test','360000199103316466','18177837777');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-01-04 21:15:10
