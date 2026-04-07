-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               11.5.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table classiq.app_user
CREATE TABLE IF NOT EXISTS `app_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table classiq.gradecategory
CREATE TABLE IF NOT EXISTS `gradecategory` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(50) NOT NULL,
  `weight` int(11) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table classiq.gradecategory_translation
CREATE TABLE IF NOT EXISTS `gradecategory_translation` (
  `category_id` int(11) NOT NULL,
  `language_code` varchar(5) NOT NULL,
  `category_name` varchar(100) NOT NULL,
  PRIMARY KEY (`category_id`,`language_code`),
  KEY `idx_gradecategory_translation_lang` (`language_code`),
  CONSTRAINT `fk_gradecategory_translation` FOREIGN KEY (`category_id`) REFERENCES `gradecategory` (`category_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table classiq.grade_scale
CREATE TABLE IF NOT EXISTS `grade_scale` (
  `scale_id` int(11) NOT NULL AUTO_INCREMENT,
  `min_mark` int(11) NOT NULL,
  `max_mark` int(11) NOT NULL,
  `grade_letter` varchar(2) NOT NULL,
  PRIMARY KEY (`scale_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table classiq.role_translation
CREATE TABLE IF NOT EXISTS `role_translation` (
  `role_code` varchar(20) NOT NULL,
  `language_code` varchar(5) NOT NULL,
  `role_name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_code`,`language_code`),
  KEY `idx_role_translation_lang` (`language_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table classiq.student
CREATE TABLE IF NOT EXISTS `student` (
  `student_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `student_number` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`student_id`),
  UNIQUE KEY `student_number` (`student_number`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `email_2` (`email`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table classiq.student_marks
CREATE TABLE IF NOT EXISTS `student_marks` (
  `marks_id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NOT NULL,
  `mathematics` int(11) DEFAULT 0,
  `english` int(11) DEFAULT 0,
  `science` int(11) DEFAULT 0,
  `craft` int(11) DEFAULT 0,
  `languages` int(11) DEFAULT 0,
  `total` int(11) DEFAULT 0,
  `average` decimal(10,2) DEFAULT 0.00,
  `feed_back` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`marks_id`),
  UNIQUE KEY `uq_student_marks` (`student_id`),
  CONSTRAINT `fk_student_marks_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table classiq.student_translation
CREATE TABLE IF NOT EXISTS `student_translation` (
  `student_id` int(11) NOT NULL,
  `language_code` varchar(5) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  PRIMARY KEY (`student_id`,`language_code`),
  KEY `idx_student_translation_lang` (`language_code`),
  CONSTRAINT `fk_student_translation_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table classiq.subject_translation
CREATE TABLE IF NOT EXISTS `subject_translation` (
  `subject_code` varchar(50) NOT NULL,
  `language_code` varchar(5) NOT NULL,
  `subject_name` varchar(100) NOT NULL,
  PRIMARY KEY (`subject_code`,`language_code`),
  KEY `idx_subject_translation_lang` (`language_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table classiq.teacher
CREATE TABLE IF NOT EXISTS `teacher` (
  `teacher_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `user_id` int(11) NOT NULL,
  `subject` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`teacher_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `email_2` (`email`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `teacher_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table classiq.teacher_marksheet
CREATE TABLE IF NOT EXISTS `teacher_marksheet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NOT NULL,
  `student_name` varchar(100) NOT NULL,
  `assignment` int(11) NOT NULL,
  `project` int(11) NOT NULL,
  `final_exam` int(11) NOT NULL,
  `total` int(11) NOT NULL,
  `grade` varchar(2) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `teacher_id` int(11) NOT NULL,
  `subject` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_tms` (`student_id`,`teacher_id`,`subject`),
  KEY `fk_tms_teacher` (`teacher_id`),
  CONSTRAINT `fk_teacher_marksheet_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  CONSTRAINT `fk_tms_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table classiq.teacher_translation
CREATE TABLE IF NOT EXISTS `teacher_translation` (
  `teacher_id` int(11) NOT NULL,
  `language_code` varchar(5) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  PRIMARY KEY (`teacher_id`,`language_code`),
  KEY `idx_teacher_translation_lang` (`language_code`),
  CONSTRAINT `fk_teacher_translation_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`teacher_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
