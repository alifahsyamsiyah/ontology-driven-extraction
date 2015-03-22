-- phpMyAdmin SQL Dump
-- version 4.3.11.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 22, 2015 at 04:16 PM
-- Server version: 5.6.23
-- PHP Version: 5.4.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `conference`
--

-- --------------------------------------------------------

--
-- Table structure for table `Acceptance`
--

CREATE TABLE IF NOT EXISTS `Acceptance` (
  `ID` int(11) NOT NULL,
  `UploadTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `User` int(11) NOT NULL,
  `Paper` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Acceptance`
--

INSERT INTO `Acceptance` (`ID`, `UploadTime`, `User`, `Paper`) VALUES
(60, '2015-03-22 15:30:20', 5, 151),
(90, '2015-03-22 15:30:27', 2, 945);

-- --------------------------------------------------------

--
-- Table structure for table `Conference`
--

CREATE TABLE IF NOT EXISTS `Conference` (
  `ID` int(11) NOT NULL,
  `Name` varchar(20) NOT NULL,
  `Organizer` int(11) NOT NULL,
  `Time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Conference`
--

INSERT INTO `Conference` (`ID`, `Name`, `Organizer`, `Time`) VALUES
(5, 'BPM 2015', 7, '2015-11-30 23:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `Decision`
--

CREATE TABLE IF NOT EXISTS `Decision` (
  `ID` varchar(20) NOT NULL,
  `DecisionTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Chair` int(11) NOT NULL,
  `Outcome` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Decision`
--

INSERT INTO `Decision` (`ID`, `DecisionTime`, `Chair`, `Outcome`) VALUES
('A02', '2015-03-24 23:00:00', 8, 0),
('A03', '2015-02-14 23:00:00', 7, 0),
('R01', '2015-03-21 16:52:21', 7, 1),
('S02', '2015-03-19 23:00:00', 8, 1);

-- --------------------------------------------------------

--
-- Table structure for table `Login`
--

CREATE TABLE IF NOT EXISTS `Login` (
  `ID` int(11) NOT NULL,
  `User` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Login`
--

INSERT INTO `Login` (`ID`, `User`) VALUES
(2, 'Diego'),
(3, 'Wil'),
(5, 'Alifah'),
(6, 'Marco'),
(7, 'John'),
(8, 'Anni');

-- --------------------------------------------------------

--
-- Table structure for table `LoginStats`
--

CREATE TABLE IF NOT EXISTS `LoginStats` (
  `ID` int(11) NOT NULL,
  `User` int(11) NOT NULL,
  `CT` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `LastAccess` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `LoginStats`
--

INSERT INTO `LoginStats` (`ID`, `User`, `CT`, `LastAccess`) VALUES
(1, 5, '2015-10-13 22:00:00', '2015-03-14 23:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `Paper`
--

CREATE TABLE IF NOT EXISTS `Paper` (
  `ID` int(11) NOT NULL,
  `Title` varchar(20) NOT NULL,
  `CT` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `User` int(11) NOT NULL,
  `Conf` int(11) NOT NULL,
  `Type` varchar(20) NOT NULL,
  `Status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Paper`
--

INSERT INTO `Paper` (`ID`, `Title`, `CT`, `User`, `Conf`, `Type`, `Status`) VALUES
(127, 'Monitoring', '2015-02-09 23:00:00', 3, 5, 'SP', 'A03'),
(151, 'Mining', '2014-11-08 23:00:00', 5, 5, 'FP', 'R01'),
(724, 'BPM', '2015-03-14 23:00:00', 3, 5, 'FM', 'A02'),
(945, 'OBDA', '2015-03-12 23:00:00', 2, 5, 'SP', 'S02');

-- --------------------------------------------------------

--
-- Table structure for table `Review`
--

CREATE TABLE IF NOT EXISTS `Review` (
  `ID` int(11) NOT NULL,
  `IDrr` int(11) NOT NULL,
  `SubmissionTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Review`
--

INSERT INTO `Review` (`ID`, `IDrr`, `SubmissionTime`) VALUES
(1, 4, '2014-11-15 23:00:00'),
(2, 8, '2015-02-12 23:00:00'),
(3, 12, '2015-03-17 23:00:00'),
(44, 16, '2015-03-22 15:29:53');

-- --------------------------------------------------------

--
-- Table structure for table `ReviewRequest`
--

CREATE TABLE IF NOT EXISTS `ReviewRequest` (
  `ID` int(11) NOT NULL,
  `InvitationTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Reviewer` int(11) NOT NULL,
  `Paper` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ReviewRequest`
--

INSERT INTO `ReviewRequest` (`ID`, `InvitationTime`, `Reviewer`, `Paper`) VALUES
(4, '2014-11-14 23:00:00', 7, 151),
(8, '2015-02-11 23:00:00', 7, 127),
(12, '2015-03-16 23:00:00', 8, 945),
(16, '2015-03-19 23:00:00', 8, 724);

-- --------------------------------------------------------

--
-- Table structure for table `Submission`
--

CREATE TABLE IF NOT EXISTS `Submission` (
  `ID` int(11) NOT NULL,
  `UploadTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `User` int(11) NOT NULL,
  `Paper` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Submission`
--

INSERT INTO `Submission` (`ID`, `UploadTime`, `User`, `Paper`) VALUES
(9, '2014-11-08 23:00:00', 5, 151),
(19, '2015-02-10 23:00:00', 3, 127),
(29, '2015-03-13 23:00:00', 2, 945),
(39, '2015-03-15 23:00:00', 3, 724);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Acceptance`
--
ALTER TABLE `Acceptance`
  ADD PRIMARY KEY (`ID`), ADD KEY `User` (`User`), ADD KEY `Paper` (`Paper`);

--
-- Indexes for table `Conference`
--
ALTER TABLE `Conference`
  ADD PRIMARY KEY (`ID`), ADD KEY `Organizer` (`Organizer`);

--
-- Indexes for table `Decision`
--
ALTER TABLE `Decision`
  ADD PRIMARY KEY (`ID`), ADD KEY `Chair` (`Chair`);

--
-- Indexes for table `Login`
--
ALTER TABLE `Login`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `LoginStats`
--
ALTER TABLE `LoginStats`
  ADD PRIMARY KEY (`ID`), ADD KEY `User` (`User`);

--
-- Indexes for table `Paper`
--
ALTER TABLE `Paper`
  ADD PRIMARY KEY (`ID`), ADD KEY `User` (`User`), ADD KEY `Conf` (`Conf`), ADD KEY `Status` (`Status`);

--
-- Indexes for table `Review`
--
ALTER TABLE `Review`
  ADD PRIMARY KEY (`ID`), ADD KEY `IDrr` (`IDrr`);

--
-- Indexes for table `ReviewRequest`
--
ALTER TABLE `ReviewRequest`
  ADD PRIMARY KEY (`ID`), ADD KEY `Reviewer` (`Reviewer`), ADD KEY `Paper` (`Paper`);

--
-- Indexes for table `Submission`
--
ALTER TABLE `Submission`
  ADD PRIMARY KEY (`ID`), ADD KEY `User` (`User`), ADD KEY `Paper` (`Paper`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Acceptance`
--
ALTER TABLE `Acceptance`
ADD CONSTRAINT `acceptance_ibfk_1` FOREIGN KEY (`User`) REFERENCES `Login` (`ID`),
ADD CONSTRAINT `acceptance_ibfk_2` FOREIGN KEY (`Paper`) REFERENCES `Paper` (`ID`);

--
-- Constraints for table `Conference`
--
ALTER TABLE `Conference`
ADD CONSTRAINT `conference_ibfk_1` FOREIGN KEY (`Organizer`) REFERENCES `Login` (`ID`);

--
-- Constraints for table `Decision`
--
ALTER TABLE `Decision`
ADD CONSTRAINT `decision_ibfk_1` FOREIGN KEY (`Chair`) REFERENCES `Login` (`ID`);

--
-- Constraints for table `LoginStats`
--
ALTER TABLE `LoginStats`
ADD CONSTRAINT `loginstats_ibfk_1` FOREIGN KEY (`User`) REFERENCES `Login` (`ID`);

--
-- Constraints for table `Paper`
--
ALTER TABLE `Paper`
ADD CONSTRAINT `paper_ibfk_1` FOREIGN KEY (`User`) REFERENCES `Login` (`ID`),
ADD CONSTRAINT `paper_ibfk_2` FOREIGN KEY (`Conf`) REFERENCES `Conference` (`ID`),
ADD CONSTRAINT `paper_ibfk_3` FOREIGN KEY (`Status`) REFERENCES `Decision` (`ID`);

--
-- Constraints for table `Review`
--
ALTER TABLE `Review`
ADD CONSTRAINT `review_ibfk_1` FOREIGN KEY (`IDrr`) REFERENCES `ReviewRequest` (`ID`);

--
-- Constraints for table `ReviewRequest`
--
ALTER TABLE `ReviewRequest`
ADD CONSTRAINT `reviewrequest_ibfk_1` FOREIGN KEY (`Reviewer`) REFERENCES `Login` (`ID`),
ADD CONSTRAINT `reviewrequest_ibfk_2` FOREIGN KEY (`Paper`) REFERENCES `Paper` (`ID`);

--
-- Constraints for table `Submission`
--
ALTER TABLE `Submission`
ADD CONSTRAINT `submission_ibfk_1` FOREIGN KEY (`User`) REFERENCES `Login` (`ID`),
ADD CONSTRAINT `submission_ibfk_2` FOREIGN KEY (`Paper`) REFERENCES `Paper` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
