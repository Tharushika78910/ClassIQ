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

-- Dumping structure for table airport_db.airports
CREATE TABLE IF NOT EXISTS `airports` (
  `AirportID` int(11) NOT NULL,
  `AirportName` varchar(100) NOT NULL,
  `CountryID` int(11) DEFAULT NULL,
  `Latitude` decimal(9,6) DEFAULT NULL,
  `Longitude` decimal(9,6) DEFAULT NULL,
  PRIMARY KEY (`AirportID`),
  KEY `CountryID` (`CountryID`),
  CONSTRAINT `airports_ibfk_1` FOREIGN KEY (`CountryID`) REFERENCES `countries` (`CountryID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Data exporting was unselected.

-- Dumping structure for table airport_db.countries
CREATE TABLE IF NOT EXISTS `countries` (
  `CountryID` int(11) NOT NULL,
  `CountryName` varchar(100) NOT NULL,
  PRIMARY KEY (`CountryID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Data exporting was unselected.

-- Dumping structure for table airport_db.players
CREATE TABLE IF NOT EXISTS `players` (
  `PlayerID` int(11) NOT NULL,
  `PlayerName` varchar(100) NOT NULL,
  `CurrentLocationID` int(11) DEFAULT NULL,
  `Concords` int(11) DEFAULT 0,
  PRIMARY KEY (`PlayerID`),
  KEY `CurrentLocationID` (`CurrentLocationID`),
  CONSTRAINT `players_ibfk_1` FOREIGN KEY (`CurrentLocationID`) REFERENCES `airports` (`AirportID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Data exporting was unselected.

-- Dumping structure for table airport_db.quests
CREATE TABLE IF NOT EXISTS `quests` (
  `QuestID` int(11) NOT NULL,
  `QuestName` varchar(100) NOT NULL,
  `Description` text DEFAULT NULL,
  `Reward` int(11) NOT NULL,
  `LocationID` int(11) DEFAULT NULL,
  PRIMARY KEY (`QuestID`),
  KEY `LocationID` (`LocationID`),
  CONSTRAINT `quests_ibfk_1` FOREIGN KEY (`LocationID`) REFERENCES `airports` (`AirportID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
