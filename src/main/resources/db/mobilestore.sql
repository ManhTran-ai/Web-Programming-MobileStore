-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: mobilestore
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quantity` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpu4bcbluhsxagirmbdn7dilm5` (`product_id`),
  KEY `FKg5uhi8vpsuy0lgloxk2h4w5o6` (`user_id`),
  CONSTRAINT `FKg5uhi8vpsuy0lgloxk2h4w5o6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKpu4bcbluhsxagirmbdn7dilm5` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'mobile'),(2,'tablet');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `price` double DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjyu2qbqt8gnvno9oe9j2s2ldk` (`order_id`),
  KEY `FK4q98utpd73imf4yhttm3w0eax` (`product_id`),
  CONSTRAINT `FK4q98utpd73imf4yhttm3w0eax` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  CONSTRAINT `FKjyu2qbqt8gnvno9oe9j2s2ldk` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` VALUES (15,15000000,3,10,31),(16,12500000,1,10,30),(17,25000000,1,10,29),(18,17000000,1,10,27),(19,15000000,1,11,31),(20,15000000,2,12,31),(21,12500000,2,12,30),(22,14500000,1,12,24),(23,15000000,1,13,28),(24,25000000,1,13,29),(25,29000000,1,13,26),(26,14500000,2,14,24),(27,15000000,1,14,31),(28,17000000,1,14,27),(29,20000000,3,15,33),(30,12500000,1,16,30),(31,20000000,1,17,33);
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `order_status` varchar(255) DEFAULT NULL,
  `order_date` datetime(6) DEFAULT NULL,
  `total_amount` double DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `payment_method` varchar(20) DEFAULT 'CASH',
  `payment_status` varchar(20) DEFAULT 'PENDING',
  `vnp_transaction_id` varchar(100) DEFAULT NULL,
  `vnp_order_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (10,'PENDING','2025-12-30 13:31:16.216000',99500000,6,'CASH','PENDING',NULL,NULL),(11,'PENDING','2026-01-10 18:04:13.043000',15000000,5,'CASH','PENDING',NULL,NULL),(12,'PENDING','2026-01-15 05:34:24.770000',69500000,6,'CASH','PENDING',NULL,NULL),(13,'PENDING','2026-01-15 05:40:28.878000',69000000,6,'CASH','PENDING',NULL,NULL),(14,'PENDING','2026-01-15 06:46:16.934000',61000000,6,'CASH','PENDING',NULL,NULL),(15,'PROCESSING','2026-01-16 08:25:12.955000',60000000,6,'CASH','PENDING',NULL,NULL),(16,'PENDING','2026-03-10 18:48:09.529000',12500000,9,'CASH','PENDING','15445629','ORDER_1773168450094'),(17,'PENDING','2026-03-11 04:43:35.762000',20000000,8,'CASH','PENDING','15446140','ORDER_1773204156553');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission` (
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES ('APPROVE_POST','approve a post'),('CREATE_DATE','create data permissions'),('REJECT_POST','reject a post');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `image` varchar(255) DEFAULT NULL,
  `manufacturer` varchar(255) NOT NULL,
  `price` float NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `product_condition` varchar(255) NOT NULL,
  `product_info` varchar(255) DEFAULT NULL,
  `quantity_in_stock` int NOT NULL,
  `category_id` int NOT NULL,
  PRIMARY KEY (`product_id`),
  KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (10,'images/products/963928d2-cfb7-4960-8d3b-eee890c82d72.png','APPLE',12000000,'iPhone 17 pro','Mới','',20,1),(11,'images/products/a773815c-3857-4466-86d5-84209c7f2b37.png','APPLE',25000000,'iPhone 15 pro','Mới','',10,1),(12,'images/products/01d57027-ac66-4333-9098-38ee7861c181.png','APPLE',7000000,'iPhone XR','Mới','',50,1),(13,'images/products/2ff2f283-7386-4e96-a91c-6d89ef259bbe.png','APPLE',9000000,'iPhone 11','Mới','',50,1),(14,'images/products/2d5c3d94-c977-4dc8-b2fa-2192fe1eb282.png','APPLE',13000000,'iPhone 13','Mới','',40,1),(15,'images/products/1693a3e0-f704-43e2-b735-d1b45b8bc5b9.png','APPLE',15000000,'iPhone 14','Mới','',30,1),(16,'images/products/51319fad-853d-4cd3-9f65-2da560db816a.png','APPLE',19000000,'iPhone 14 Pro','Mới','',25,1),(17,'images/products/0b7a70ec-43ce-48d4-b1f4-acf7a28b2ab3.png','APPLE',18000000,'iPhone 15','Mới','',30,1),(18,'images/products/33aca89a-fbc1-4921-829c-4e5b9137d9eb.png','APPLE',28000000,'iPhone 15 Pro Max','Mới','',20,1),(19,'images/products/0f11c558-01e9-4d63-a30c-77f597d98238.png','APPLE',22000000,'iPhone 16','Mới','',50,1),(20,'images/products/5a37c593-51bf-4b7b-adbc-d0bfcd620f8e.png','APPLE',24000000,'iPhone 16 Plus','Mới','',30,1),(21,'images/products/1ecdc28a-6a10-4bec-a7d6-6ad8a89af80a.png','APPLE',26000000,'iPhone 17','Mới','',100,1),(23,'images/products/b6dc74a6-da46-467d-aaa5-533b18c859fa.png','APPLE',10500000,'iPad Gen 10','Mới','',40,2),(24,'images/products/0af0a633-b612-4218-b042-ef822e99264f.png','APPLE',14500000,'iPad Air 5 M1','Mới','',22,2),(25,'images/products/de33773e-bc38-46c8-a7f0-cd1e7e71d655.png','APPLE',7500000,'iPad Gen 9','Mới','',50,2),(26,'images/products/prom4.png','APPLE',29000000,'iPad Pro M4','Mới','',14,2),(27,'images/products/0c334961-a3e2-4b40-93f1-b45bce5ce52a.png','APPLE',17000000,'iPad Air M2','Mới','',18,2),(28,'images/products/mini7.png','APPLE',15000000,'iPad mini 7','Mới','',19,2),(29,'images/products/mini_6.png','APPLE',25000000,'iPad mini 6','Mới','',28,2),(30,'images/products/12.png','APPLE',12500000,'iPhone 12','Mới','',26,1),(31,'images/products/16e.png','APPLE',15000000,'iPhone 16e','Mới','',13,1),(32,'images/products/4eef0b69-5658-4014-b9ff-d65881d378b3.png','APPLE',500000000,'iPad M4 Pro','Mới','',100,2),(33,'images/products/07fb6758-a24f-4993-8141-92dcbe635838.png','APPLE',20000000,'iPad M5 Pro','Mới','',46,2);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES ('ADMIN','admin role'),('CUSTOMER','customer role');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles_permissions`
--

DROP TABLE IF EXISTS `roles_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles_permissions` (
  `role_name` varchar(255) NOT NULL,
  `permissions_name` varchar(255) NOT NULL,
  PRIMARY KEY (`role_name`,`permissions_name`),
  KEY `FKpqh0ean4n5un6h790yuaimjj8` (`permissions_name`),
  CONSTRAINT `FK6nw4jrj1tuu04j9rk7xwfssd6` FOREIGN KEY (`role_name`) REFERENCES `roles` (`name`),
  CONSTRAINT `FKpqh0ean4n5un6h790yuaimjj8` FOREIGN KEY (`permissions_name`) REFERENCES `permission` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles_permissions`
--

LOCK TABLES `roles_permissions` WRITE;
/*!40000 ALTER TABLE `roles_permissions` DISABLE KEYS */;
INSERT INTO `roles_permissions` VALUES ('ADMIN','APPROVE_POST'),('ADMIN','CREATE_DATE'),('CUSTOMER','CREATE_DATE'),('ADMIN','REJECT_POST');
/*!40000 ALTER TABLE `roles_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `oauth_provider` varchar(20) DEFAULT NULL,
  `oauth_id` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `FK6e7f1kfvvn2k48olww485qvo3` (`role_name`),
  CONSTRAINT `FK6e7f1kfvvn2k48olww485qvo3` FOREIGN KEY (`role_name`) REFERENCES `roles` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (5,'$2a$10$342ro1UObsU/8YP0Dy1HNOQ92Fxy3hYI/KLTc0ygh7j5Q6NyeIyP6','levantai','ADMIN',NULL,NULL,NULL),(6,'$2a$10$w0KzLYHDgs5PI0Q4r3BjRu9RM3UbNa4IHSnfIb74KrHsTAlOnRfzW','mạnh','CUSTOMER',NULL,NULL,NULL),(7,'$2a$10$bj4SkPMUK3jTJ7FD.9blzeYmCo.bUF5vd1wJVh2ldpIxQ8F5DHetG','Hưng','CUSTOMER',NULL,NULL,NULL),(8,NULL,'Tài','CUSTOMER','google','116054212909485433236','levantai066@gmail.com'),(9,NULL,'Tài Lê Văn','CUSTOMER','google','110613103348013667969','23130283@st.hcmuaf.edu.vn');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-13 14:30:33
