-- MySQL dump 10.13  Distrib 5.6.16, for Linux (x86_64)
--
-- Host: localhost    Database: jmanager_test
-- ------------------------------------------------------
-- Server version	5.6.16-debug-log

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
-- Table structure for table `datanode_info`
--

DROP TABLE IF EXISTS `datanode_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `datanode_info` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `instance_id` char(32) NOT NULL COMMENT 'instance_talbe表中的id字段',
  `datanode_name` varchar(70) NOT NULL COMMENT '数据库节点名,即数据库名',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `instance_id_datanode` (`instance_id`,`datanode_name`),
  CONSTRAINT `datanode_info_1` FOREIGN KEY (`instance_id`) REFERENCES `mysql_instance` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datanode_info`
--

LOCK TABLES `datanode_info` WRITE;
/*!40000 ALTER TABLE `datanode_info` DISABLE KEYS */;
INSERT INTO `datanode_info` VALUES ('014f2f8c2ca14b42a02d030efdc9a815','18fe019859694ff790ed0272fd8f714e','proxy3','2016-09-04 12:11:32','2016-09-04 12:11:32'),('757999c9829849b596792e81c085741f','18fe019859694ff790ed0272fd8f714e','proxy4','2016-09-04 12:11:32','2016-09-04 12:11:32'),('75b187e7742311e6bf41507b9d578e91','18fe019859694ff790ed0272fd8f714e','proxy','2016-09-06 11:17:12','2016-09-06 11:17:12'),('983d9601759f11e6bf41507b9d578e91','18fe019859694ff790ed0272fd8f714e','proxy_sp','2016-09-08 08:38:19','2016-09-08 08:38:19'),('9c7eff74ac4b47ce9221ec95dd49517c','18fe019859694ff790ed0272fd8f714e','proxy2','2016-09-04 12:11:32','2016-09-04 12:11:32'),('e07c587a8fd144b4b0a7e91ee0e23542','18fe019859694ff790ed0272fd8f714e','proxy1','2016-09-04 12:11:32','2016-09-04 12:11:32');
/*!40000 ALTER TABLE `datanode_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `func_class_info`
--

DROP TABLE IF EXISTS `func_class_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `func_class_info` (
  `id` char(32) NOT NULL COMMENT '主键id',
  `class` varchar(255) NOT NULL COMMENT '函数类名,类一般有系统提供，也可以自拟定添加， 默认只提供string,long',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `class` (`class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `func_class_info`
--

LOCK TABLES `func_class_info` WRITE;
/*!40000 ALTER TABLE `func_class_info` DISABLE KEYS */;
INSERT INTO `func_class_info` VALUES ('3048c6e958514234a691b81d71d18b8a','com.jd.jproxy.route.function.PartitionByLong','2014-02-18 18:35:06','2014-02-18 18:35:06'),('9c6244210b51461cbf206443eeeba93e','com.jd.jproxy.route.function.PartitionByString','2014-02-18 18:35:06','2014-02-18 18:35:06');
/*!40000 ALTER TABLE `func_class_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jproxy_user`
--

DROP TABLE IF EXISTS `jproxy_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jproxy_user` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `username` varchar(255) NOT NULL COMMENT 'jproxy用户名',
  `passwd` varchar(255) NOT NULL COMMENT 'jproxy密码',
  `read_only` int(11) NOT NULL DEFAULT '0' COMMENT '用户权限 1只读，0可读可写',
  `select_max_rows` int(11) NOT NULL DEFAULT '-1' COMMENT '查询记录限制, -1为不做限制',
  `allowed_hosts` varchar(255) DEFAULT NULL COMMENT '允许的客户端ip',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jproxy_user`
--

LOCK TABLES `jproxy_user` WRITE;
/*!40000 ALTER TABLE `jproxy_user` DISABLE KEYS */;
INSERT INTO `jproxy_user` VALUES ('a4e5f4bf745d47abb8c887aca8838cec','root','*81F5E21E35407D884A6CD4A731AEBFB6AF209E1B',0,-1,'127;10;172','2016-09-04 12:11:32','2016-09-04 12:11:32');
/*!40000 ALTER TABLE `jproxy_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mysql_instance`
--

DROP TABLE IF EXISTS `mysql_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mysql_instance` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `instance_name` varchar(70) NOT NULL COMMENT 'mysql实例名',
  `ip` char(255) NOT NULL COMMENT 'instance ip',
  `port` int(11) NOT NULL COMMENT '端口',
  `username` varchar(70) NOT NULL COMMENT '后端mysql用户名',
  `passwd` varchar(70) NOT NULL COMMENT '后端mysql密码',
  `sql_mode` varchar(70) NOT NULL DEFAULT 'STRICT_TRANS_TABLES' COMMENT 'mysql sql检测级别',
  `init_size` int(11) NOT NULL DEFAULT '10' COMMENT '连接池初始化链接数',
  `pool_size` int(11) NOT NULL DEFAULT '256' COMMENT '保持后端数据通道的默认最大值',
  `idle_timeout` int(11) NOT NULL DEFAULT '1800000' COMMENT '超时时间单位为毫秒',
  `wait_timeout` int(11) NOT NULL DEFAULT '1000' COMMENT '取得新连接的等待超时时间',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `instance_name` (`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mysql_instance`
--

LOCK TABLES `mysql_instance` WRITE;
/*!40000 ALTER TABLE `mysql_instance` DISABLE KEYS */;
INSERT INTO `mysql_instance` VALUES ('18fe019859694ff790ed0272fd8f714e','127.0.0.1.root','127.0.0.1',3306,'root','secret','STRICT_TRANS_TABLES',10,256,1800000,1000,'2016-09-04 12:11:32','2016-09-04 12:11:32');
/*!40000 ALTER TABLE `mysql_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `privilege_info`
--

DROP TABLE IF EXISTS `privilege_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `privilege_info` (
  `id` char(32) NOT NULL,
  `user_id` char(32) NOT NULL,
  `insert_priv` enum('N','Y') NOT NULL DEFAULT 'N' COMMENT 'insert ',
  `delete_priv` enum('N','Y') NOT NULL DEFAULT 'N' COMMENT 'delete ',
  `update_priv` enum('N','Y') NOT NULL DEFAULT 'N' COMMENT 'update ',
  `select_priv` enum('N','Y') NOT NULL DEFAULT 'N' COMMENT 'select ',
  `insert_with_shard_key_priv` enum('N','Y') NOT NULL DEFAULT 'Y' COMMENT 'insert  ',
  `delete_with_shard_key_priv` enum('N','Y') NOT NULL DEFAULT 'Y' COMMENT 'delete  ',
  `update_with_shard_key_priv` enum('N','Y') NOT NULL DEFAULT 'Y' COMMENT 'update  ',
  `select_with_shard_key_priv` enum('N','Y') NOT NULL DEFAULT 'Y' COMMENT 'select  ',
  PRIMARY KEY (`id`),
  UNIQUE KEY `priviege_unique_idx` (`user_id`),
  CONSTRAINT `privilege_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `jproxy_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `privilege_info`
--

LOCK TABLES `privilege_info` WRITE;
/*!40000 ALTER TABLE `privilege_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `privilege_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proxy_info`
--

DROP TABLE IF EXISTS `proxy_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proxy_info` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `proxy_ip` char(15) NOT NULL COMMENT 'proxyIP',
  `server_port` int(11) NOT NULL DEFAULT '4066' COMMENT '服务端口',
  `manager_port` int(11) NOT NULL DEFAULT '5066' COMMENT '管理端口',
  `monitor_port` int(11) NOT NULL DEFAULT '5067' COMMENT '监控端口',
  `proxy_username` varchar(70) NOT NULL COMMENT 'proxy管理用户',
  `proxy_passwd` varchar(70) NOT NULL COMMENT 'proxy密码',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proxy_info`
--

LOCK TABLES `proxy_info` WRITE;
/*!40000 ALTER TABLE `proxy_info` DISABLE KEYS */;
INSERT INTO `proxy_info` VALUES ('87a572e4365e4dcca4ec1bd482ff2007','127.0.0.1',4066,5066,8888,'jmanager','secret','2016-09-05 10:12:36','2016-09-05 10:12:36');
/*!40000 ALTER TABLE `proxy_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `route_change_status`
--

DROP TABLE IF EXISTS `route_change_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `route_change_status` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `trans_plan_id` char(32) NOT NULL COMMENT 'transfer_plan表中的id',
  `trans_plan_status` int(11) NOT NULL COMMENT 'locking 1, sync_validating 2, route_updating 3, unlocking 4,  aborted 0',
  `trans_plan_start` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '任务开始时间',
  `trans_plan_end` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '任务结束时间',
  `is_active` tinyint(4) NOT NULL DEFAULT '0' COMMENT '任务状态置位，表中最多仅有一条记录为1，其余已完成或者失败的记录都为0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `route_change_status`
--

LOCK TABLES `route_change_status` WRITE;
/*!40000 ALTER TABLE `route_change_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `route_change_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_func_info`
--

DROP TABLE IF EXISTS `rule_func_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rule_func_info` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `func_class_id` varchar(255) NOT NULL COMMENT 'func_class表id字段',
  `partition_count1` int(11) DEFAULT NULL COMMENT '数据库节点数',
  `partition_weight1` varchar(255) DEFAULT NULL COMMENT '每个数据库节点所占比重',
  `partition_count2` int(11) DEFAULT NULL COMMENT '数据库节点数',
  `partition_weight2` varchar(255) DEFAULT NULL COMMENT '每个数据库节点所占比重',
  `start_date` varchar(255) DEFAULT NULL COMMENT '开始日期',
  `new_start` varchar(255) DEFAULT NULL,
  `period` int(11) DEFAULT NULL COMMENT '以月份为单位',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `rule_func_1` (`func_class_id`),
  CONSTRAINT `rule_func_1` FOREIGN KEY (`func_class_id`) REFERENCES `func_class_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_func_info`
--

LOCK TABLES `rule_func_info` WRITE;
/*!40000 ALTER TABLE `rule_func_info` DISABLE KEYS */;
INSERT INTO `rule_func_info` VALUES ('a8c4ac8a419346e9a41c60d6c427b601','3048c6e958514234a691b81d71d18b8a',4,'256',NULL,NULL,NULL,NULL,NULL,'2016-09-04 12:11:32','2016-09-04 12:11:32'),('b05c3ce9759011e6bf41507b9d578e91','3048c6e958514234a691b81d71d18b8a',1,'1024',NULL,NULL,NULL,NULL,NULL,'2016-09-08 06:51:37','2016-09-08 06:51:37'),('f72fc05c5e3e404ea390f48dddbf0bf0','9c6244210b51461cbf206443eeeba93e',4,'256',NULL,NULL,NULL,NULL,NULL,'2016-09-06 08:42:51','2016-09-06 08:42:51');
/*!40000 ALTER TABLE `rule_func_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rule_info`
--

DROP TABLE IF EXISTS `rule_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rule_info` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `columns` varchar(255) NOT NULL COMMENT '路由字段, 复合路由字段通过逗号分割',
  `rule_func_id` char(32) NOT NULL COMMENT 'rule_func表中id字段',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `rule_1` (`rule_func_id`),
  CONSTRAINT `rule_1` FOREIGN KEY (`rule_func_id`) REFERENCES `rule_func_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule_info`
--

LOCK TABLES `rule_info` WRITE;
/*!40000 ALTER TABLE `rule_info` DISABLE KEYS */;
INSERT INTO `rule_info` VALUES ('260c5144e8534660ad39a62e92d36822','create_time','a8c4ac8a419346e9a41c60d6c427b601','2016-09-06 08:42:51','2016-09-06 08:42:51'),('2d4c4b41f55d4dafbbc52ffdf3b234f4','id','a8c4ac8a419346e9a41c60d6c427b601','2016-09-04 12:11:32','2016-09-04 12:11:32'),('2f46d2d6759011e6bf41507b9d578e91','int_sp','b05c3ce9759011e6bf41507b9d578e91','2016-09-08 06:48:01','2016-09-08 06:53:39'),('c5fc66b14ee74b22bb591ef7fd7ffaa7','business_id','f72fc05c5e3e404ea390f48dddbf0bf0','2016-09-06 08:42:51','2016-09-06 08:42:51');
/*!40000 ALTER TABLE `rule_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schema_info`
--

DROP TABLE IF EXISTS `schema_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schema_info` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `schema_name` varchar(70) NOT NULL COMMENT '逻辑库名',
  `max_capacity` int(11) NOT NULL,
  `max_conns` int(11) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `schema_name` (`schema_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schema_info`
--

LOCK TABLES `schema_info` WRITE;
/*!40000 ALTER TABLE `schema_info` DISABLE KEYS */;
INSERT INTO `schema_info` VALUES ('dc27807b4758450189f45808775978a2','proxy',1024,1000,'2016-09-04 12:11:32','2016-09-04 12:11:32');
/*!40000 ALTER TABLE `schema_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `special_route`
--

DROP TABLE IF EXISTS `special_route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `special_route` (
  `id` char(32) NOT NULL COMMENT 'id',
  `table_id` char(32) NOT NULL COMMENT 'id',
  `column_name` varchar(255) NOT NULL,
  `column_value` varchar(255) NOT NULL,
  `datanode_id` char(32) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `special_route`
--

LOCK TABLES `special_route` WRITE;
/*!40000 ALTER TABLE `special_route` DISABLE KEYS */;
INSERT INTO `special_route` VALUES ('5643820075a711e6bf41507b9d578e91','f220a40975a611e6bf41507b9d578e91','NO_RULE_SP','364609','014f2f8c2ca14b42a02d030efdc9a815','2016-09-08 09:33:44','2016-09-08 09:33:44'),('5643881c75a711e6bf41507b9d578e91','f220a40975a611e6bf41507b9d578e91','NO_RULE_SP','353730','757999c9829849b596792e81c085741f','2016-09-08 09:33:44','2016-09-08 09:33:44'),('5643919975a711e6bf41507b9d578e91','f220a40975a611e6bf41507b9d578e91','NO_RULE_SP','364605','75b187e7742311e6bf41507b9d578e91','2016-09-08 09:33:44','2016-09-08 09:33:44'),('5643a42275a711e6bf41507b9d578e91','f220a40975a611e6bf41507b9d578e91','NO_RULE_SP','422835','9c7eff74ac4b47ce9221ec95dd49517c','2016-09-08 09:33:44','2016-09-08 09:33:44'),('5643a9b175a711e6bf41507b9d578e91','f220a40975a611e6bf41507b9d578e91','NO_RULE_SP','387136','e07c587a8fd144b4b0a7e91ee0e23542','2016-09-08 09:33:44','2016-09-08 09:33:44'),('b53ebc1c74ad11e6bf41507b9d578e91','4df53e9d74ad11e6bf41507b9d578e91','INT_SP','364609','014f2f8c2ca14b42a02d030efdc9a815','2016-09-07 12:54:33','2016-09-07 12:54:33'),('bcfb852a74ad11e6bf41507b9d578e91','4df53e9d74ad11e6bf41507b9d578e91','INT_SP','353730','757999c9829849b596792e81c085741f','2016-09-07 12:37:02','2016-09-07 12:37:02'),('c3c0dfbf74ad11e6bf41507b9d578e91','4df53e9d74ad11e6bf41507b9d578e91','INT_SP','364605','75b187e7742311e6bf41507b9d578e91','2016-09-07 11:12:39','2016-09-07 11:12:39'),('cb4cd21874ad11e6bf41507b9d578e91','4df53e9d74ad11e6bf41507b9d578e91','INT_SP','422835','9c7eff74ac4b47ce9221ec95dd49517c','2016-09-07 12:37:48','2016-09-07 12:37:48'),('d1d62f4d74ad11e6bf41507b9d578e91','4df53e9d74ad11e6bf41507b9d578e91','INT_SP','387136','e07c587a8fd144b4b0a7e91ee0e23542','2016-09-07 12:38:19','2016-09-07 12:38:19');
/*!40000 ALTER TABLE `special_route` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_datanode_relation`
--

DROP TABLE IF EXISTS `table_datanode_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `table_datanode_relation` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `table_id` char(32) NOT NULL COMMENT 'table_info表中的id字段',
  `datanode_id` char(32) NOT NULL COMMENT 'datanode_info表中的id字段',
  `datanode_idx` int(11) NOT NULL COMMENT 'datanode_info排序字段',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `table_datanode_name` (`table_id`,`datanode_id`),
  KEY `table_datanode_relation_2` (`datanode_id`),
  CONSTRAINT `table_datanode_relation_1` FOREIGN KEY (`table_id`) REFERENCES `table_info` (`id`),
  CONSTRAINT `table_datanode_relation_2` FOREIGN KEY (`datanode_id`) REFERENCES `datanode_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_datanode_relation`
--

LOCK TABLES `table_datanode_relation` WRITE;
/*!40000 ALTER TABLE `table_datanode_relation` DISABLE KEYS */;
INSERT INTO `table_datanode_relation` VALUES ('121661c8112b4b639be4c2cda8411cb0','12cd0ea8985d4ca19c09ad620a4ae23f','757999c9829849b596792e81c085741f',4,'2016-09-06 08:42:51'),('2c7fc072f8c44485918dddaf023045fc','90d5eabf92c9410f8c91283cd5f7c4f2','e07c587a8fd144b4b0a7e91ee0e23542',1,'2016-09-06 08:42:51'),('2f1d820dfd124ba1845716cd069b366c','44a8acb30a0348d2baaedaa497f409cb','e07c587a8fd144b4b0a7e91ee0e23542',1,'2016-09-04 12:11:32'),('3e960eb0dc5c4488b203950430d1ca2c','626dea7fc5734d91ae9c99c67cfa65d7','9c7eff74ac4b47ce9221ec95dd49517c',2,'2016-09-06 08:42:51'),('45510250973543ce80820297e53cc5e7','90d5eabf92c9410f8c91283cd5f7c4f2','014f2f8c2ca14b42a02d030efdc9a815',3,'2016-09-06 08:42:51'),('5d4fc8185f6d4b09a4f2159e882221a7','12cd0ea8985d4ca19c09ad620a4ae23f','9c7eff74ac4b47ce9221ec95dd49517c',2,'2016-09-06 08:42:51'),('80757b3850734739b9492c8cce95d7cd','44a8acb30a0348d2baaedaa497f409cb','014f2f8c2ca14b42a02d030efdc9a815',3,'2016-09-04 12:11:32'),('8c9c06ecf322442f89ea2eb522912e4f','90d5eabf92c9410f8c91283cd5f7c4f2','757999c9829849b596792e81c085741f',4,'2016-09-06 08:42:51'),('92e680760e6345fea06baed0b917099a','44a8acb30a0348d2baaedaa497f409cb','757999c9829849b596792e81c085741f',4,'2016-09-04 12:11:32'),('a63d1628758d11e6bf41507b9d578e91','4df53e9d74ad11e6bf41507b9d578e91','983d9601759f11e6bf41507b9d578e91',1,'2016-09-08 06:29:52'),('aa1498e8742311e6bf41507b9d578e91','23e55f85742311e6bf41507b9d578e91','75b187e7742311e6bf41507b9d578e91',1,'2016-09-06 11:18:40'),('af3c1790550d411f979f892a5bb23f49','626dea7fc5734d91ae9c99c67cfa65d7','e07c587a8fd144b4b0a7e91ee0e23542',1,'2016-09-06 08:42:51'),('b5f5406baceb497590b953c44f453a06','90d5eabf92c9410f8c91283cd5f7c4f2','9c7eff74ac4b47ce9221ec95dd49517c',2,'2016-09-06 08:42:51'),('c3b1938e5f05405088d15809b451283b','626dea7fc5734d91ae9c99c67cfa65d7','014f2f8c2ca14b42a02d030efdc9a815',3,'2016-09-06 08:42:51'),('c5b3490fcbee4b12a6be73e60e017fc0','44a8acb30a0348d2baaedaa497f409cb','9c7eff74ac4b47ce9221ec95dd49517c',2,'2016-09-04 12:11:32'),('c647d6a7bd494460abad9c9eefbb0c8b','626dea7fc5734d91ae9c99c67cfa65d7','757999c9829849b596792e81c085741f',4,'2016-09-06 08:42:51'),('c9965e0d40af4301adcb3721d998449a','12cd0ea8985d4ca19c09ad620a4ae23f','e07c587a8fd144b4b0a7e91ee0e23542',1,'2016-09-06 08:42:51'),('d7cff68b8cbe4690a7b6f79c9b35b672','12cd0ea8985d4ca19c09ad620a4ae23f','014f2f8c2ca14b42a02d030efdc9a815',3,'2016-09-06 08:42:51');
/*!40000 ALTER TABLE `table_datanode_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_info`
--

DROP TABLE IF EXISTS `table_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `table_info` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `table_name` varchar(70) NOT NULL COMMENT '逻辑表名',
  `schema_id` char(32) NOT NULL COMMENT 'schema_info表中id',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `schema_table_name` (`schema_id`,`table_name`),
  CONSTRAINT `table_info_1` FOREIGN KEY (`schema_id`) REFERENCES `schema_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_info`
--

LOCK TABLES `table_info` WRITE;
/*!40000 ALTER TABLE `table_info` DISABLE KEYS */;
INSERT INTO `table_info` VALUES ('12cd0ea8985d4ca19c09ad620a4ae23f','date_shard','dc27807b4758450189f45808775978a2','2016-09-06 08:42:51','2016-09-06 08:42:51'),('23e55f85742311e6bf41507b9d578e91','no_shard','dc27807b4758450189f45808775978a2','2016-09-06 11:14:55','2016-09-06 11:14:55'),('44a8acb30a0348d2baaedaa497f409cb','report','dc27807b4758450189f45808775978a2','2016-09-04 12:11:32','2016-09-04 12:11:32'),('4df53e9d74ad11e6bf41507b9d578e91','int_special','dc27807b4758450189f45808775978a2','2016-09-07 03:43:56','2016-09-07 03:43:56'),('626dea7fc5734d91ae9c99c67cfa65d7','int_shard','dc27807b4758450189f45808775978a2','2016-09-06 08:42:51','2016-09-06 08:42:51'),('90d5eabf92c9410f8c91283cd5f7c4f2','str_shard','dc27807b4758450189f45808775978a2','2016-09-06 08:42:51','2016-09-06 08:42:51'),('f220a40975a611e6bf41507b9d578e91','no_rule_special','dc27807b4758450189f45808775978a2','2016-09-08 09:30:56','2016-09-08 09:30:56');
/*!40000 ALTER TABLE `table_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_rule_relation`
--

DROP TABLE IF EXISTS `table_rule_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `table_rule_relation` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `table_id` char(32) NOT NULL COMMENT '路由规则名称',
  `rule_id` char(32) NOT NULL COMMENT '路由规则名称',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `table_id` (`table_id`,`rule_id`),
  KEY `table_rule_relation_1` (`table_id`),
  KEY `table_rule_relation_2` (`rule_id`),
  CONSTRAINT `table_rule_relation_1` FOREIGN KEY (`table_id`) REFERENCES `table_info` (`id`),
  CONSTRAINT `table_rule_relation_2` FOREIGN KEY (`rule_id`) REFERENCES `rule_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_rule_relation`
--

LOCK TABLES `table_rule_relation` WRITE;
/*!40000 ALTER TABLE `table_rule_relation` DISABLE KEYS */;
INSERT INTO `table_rule_relation` VALUES ('0f516b2f50cf4050a193b7b986331b93','44a8acb30a0348d2baaedaa497f409cb','2d4c4b41f55d4dafbbc52ffdf3b234f4','2016-09-04 12:11:32','2016-09-04 12:11:32'),('364d61c733f04fd8a64b05c6ceea9464','12cd0ea8985d4ca19c09ad620a4ae23f','260c5144e8534660ad39a62e92d36822','2016-09-06 08:42:51','2016-09-06 08:42:52'),('482bf58e23a84d1795eb8b7c1fb162c8','90d5eabf92c9410f8c91283cd5f7c4f2','c5fc66b14ee74b22bb591ef7fd7ffaa7','2016-09-06 08:42:51','2016-09-06 08:42:52'),('6e66d07e759011e6bf41507b9d578e91','4df53e9d74ad11e6bf41507b9d578e91','2f46d2d6759011e6bf41507b9d578e91','2016-09-08 06:49:46','2016-09-08 06:49:46'),('a7756f27781e4a18ab7435af240edb61','626dea7fc5734d91ae9c99c67cfa65d7','2d4c4b41f55d4dafbbc52ffdf3b234f4','2016-09-06 08:42:51','2016-09-06 08:42:52');
/*!40000 ALTER TABLE `table_rule_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transfer_plan`
--

DROP TABLE IF EXISTS `transfer_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transfer_plan` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `datanode_id` char(32) NOT NULL COMMENT '数据库节点的id',
  `datanode_name` varchar(70) NOT NULL COMMENT '数据库节点名',
  `src_instance_id` char(32) NOT NULL COMMENT '源mysql实例id',
  `dst_instance_id` char(32) NOT NULL COMMENT '目标mysql实例id',
  `schedule_time` datetime NOT NULL COMMENT '定时执行任务的时间',
  `finish_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '完成时间',
  `status` int(11) NOT NULL COMMENT 'pending 10, env_checking 20, dumping, restoring, full_validating, catching, write_locking, sync_validating, route_cutting, finished, canceled, aborted 120 ',
  `status_err` varchar(70) NOT NULL DEFAULT '' COMMENT '迁移任务错误信息',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transfer_plan`
--

LOCK TABLES `transfer_plan` WRITE;
/*!40000 ALTER TABLE `transfer_plan` DISABLE KEYS */;
/*!40000 ALTER TABLE `transfer_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transfer_rollback`
--

DROP TABLE IF EXISTS `transfer_rollback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transfer_rollback` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `transfer_plan_id` char(32) NOT NULL COMMENT '迁移任务ID',
  `src_instance_id` char(32) NOT NULL COMMENT '源数据库实例ID',
  `dst_instance_id` char(32) NOT NULL COMMENT '目标数据库实例ID',
  `binlog_file_name` varchar(128) NOT NULL COMMENT '目标数据库实例的binlog的文件名',
  `binlog_pos` bigint(20) NOT NULL COMMENT '目标数据库的binlog的位置',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transfer_rollback`
--

LOCK TABLES `transfer_rollback` WRITE;
/*!40000 ALTER TABLE `transfer_rollback` DISABLE KEYS */;
/*!40000 ALTER TABLE `transfer_rollback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_schema_relation`
--

DROP TABLE IF EXISTS `user_schema_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_schema_relation` (
  `id` char(32) NOT NULL COMMENT '主键ID',
  `jproxy_user_id` char(32) NOT NULL COMMENT 'jproxy_user表中的id字段',
  `schema_id` char(32) NOT NULL COMMENT 'schema_info表中的id字段',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `jproxy_user_id` (`jproxy_user_id`,`schema_id`),
  KEY `jproxy_user_schema_1` (`jproxy_user_id`),
  KEY `jproxy_user_schema_2` (`schema_id`),
  CONSTRAINT `jproxy_user_schema_1` FOREIGN KEY (`jproxy_user_id`) REFERENCES `jproxy_user` (`id`),
  CONSTRAINT `jproxy_user_schema_2` FOREIGN KEY (`schema_id`) REFERENCES `schema_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_schema_relation`
--

LOCK TABLES `user_schema_relation` WRITE;
/*!40000 ALTER TABLE `user_schema_relation` DISABLE KEYS */;
INSERT INTO `user_schema_relation` VALUES ('34e53c5db5f748958f066688918bf9df','a4e5f4bf745d47abb8c887aca8838cec','dc27807b4758450189f45808775978a2','2016-09-04 12:11:32','2016-09-04 12:11:32');
/*!40000 ALTER TABLE `user_schema_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `virtual_ip`
--

DROP TABLE IF EXISTS `virtual_ip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `virtual_ip` (
  `id` char(32) NOT NULL,
  `vip` varchar(255) NOT NULL,
  `port` int(11) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `virtual_ip`
--

LOCK TABLES `virtual_ip` WRITE;
/*!40000 ALTER TABLE `virtual_ip` DISABLE KEYS */;
/*!40000 ALTER TABLE `virtual_ip` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-09-12 20:50:49
