CREATE TABLE `party` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `logo_url` text NOT NULL,
  `name` varchar(255) NOT NULL,
  `president_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci