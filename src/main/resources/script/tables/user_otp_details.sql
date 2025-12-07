CREATE TABLE `user_otp_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `expiry_time` bigint DEFAULT NULL,
  `is_otp_used` bit(1) NOT NULL,
  `otp` int DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6jicgnjchsq357ivolb99a2b4` (`user_id`),
  CONSTRAINT `FK6jicgnjchsq357ivolb99a2b4` FOREIGN KEY (`user_id`) REFERENCES `user_detail` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci