drop database IF EXISTS `forums`;
create DATABASE `forums`;
USE `forums`;

create TABLE `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(50) NOT NULL,
  `password` VARCHAR(50) NOT NULL,
  `name` VARCHAR(50) DEFAULT NULL,
  `email` VARCHAR(50) NOT NULL,
  `userType` ENUM('USER','SUPERUSER') DEFAULT 'USER',
  `timeRegistered` DATETIME DEFAULT NULL,
  `restrictionType` ENUM('FULL','LIM','MAXLIM') DEFAULT 'FULL',
  `banCount` INT(4) NOT NULL DEFAULT '5',
  `banTime` DATETIME DEFAULT NULL,
  `deleted` TINYINT(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login` (`login`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE `session` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `cookie` VARCHAR(50) NOT NULL,
  `userid` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user` (`userid`),
  KEY `cookie` (`cookie`),
  CONSTRAINT `usersession` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON delete CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE `forum` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `userid` INT(11) NOT NULL,
  `topic` VARCHAR(100) NOT NULL,
  `forumType` ENUM('MODERATED','UNMODERATED') NOT NULL DEFAULT 'MODERATED',
  `readonly` TINYINT(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueTopic` (`topic`),
  KEY `user` (`userid`),
  CONSTRAINT `userforum` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON delete CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;


create TABLE `message_header` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `forumid` INT(11) NOT NULL,
  `priority` ENUM('HIGH','NORMAL','LOW') NOT NULL DEFAULT 'NORMAL',
  `topic` VARCHAR(500) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `forumid` (`forumid`),
  CONSTRAINT `forummessage` FOREIGN KEY (`forumid`) REFERENCES `forum` (`id`) ON delete CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE `message_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `header` INT(11) NOT NULL,
  `parent` INT(11) DEFAULT NULL,
  `userid` INT(11) NOT NULL,
  `timeCreate` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user` (`userid`),
  KEY `message_info_header` (`header`),
  KEY `message_root_info` (`parent`),
  CONSTRAINT `message_info_header` FOREIGN KEY (`header`) REFERENCES `message_header` (`id`) ON delete CASCADE,
  CONSTRAINT `message_root_info` FOREIGN KEY (`parent`) REFERENCES `message_info` (`id`) ON delete CASCADE,
  CONSTRAINT `usermessage` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON delete CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE `message_text` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `infoid` INT(11) NOT NULL,
  `state` ENUM('PUBLISHED','UNPUBLISHED') NOT NULL DEFAULT 'UNPUBLISHED',
  `body` VARCHAR(500) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `infokey` (`infoid`),
  CONSTRAINT `infomessage` FOREIGN KEY (`infoid`) REFERENCES `message_info` (`id`) ON delete CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE `message_tags` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `header` INT(11) NOT NULL,
  `tag` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `header_tag` (`header`,`tag`),
  KEY `headertags` (`header`),
  CONSTRAINT `message_tags_ibfk_1` FOREIGN KEY (`header`) REFERENCES `message_header` (`id`) ON delete CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create TABLE `message_rating` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `infoid` INT(11) NOT NULL,
  `userid` INT(11) NOT NULL,
  `rating` INT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `infokey` (`infoid`,`userid`),
  KEY `user` (`userid`),
  CONSTRAINT `message_info_ibfk_3` FOREIGN KEY (`infoid`) REFERENCES `message_info` (`id`) ON delete CASCADE,
  CONSTRAINT `message_rating_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON delete CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create view `forumview` AS (
select
  `forum`.`id`    AS `id`,
  `forum`.`topic` AS `topic`,
  COUNT(DISTINCT `message_header`.`id`) AS `countMessages`,
  COUNT(`message_info`.`parent`) AS `countComments`
FROM ((`message_header`
    JOIN `forum`
      ON ((`message_header`.`forumid` = `forum`.`id`)))
   JOIN `message_info`
     ON ((`message_info`.`header` = `message_header`.`id`)))
GROUP BY `forum`.`id`);

create view `fullmessageview` AS (
select
  `message_info`.`id`         AS `id`,
  `message_header`.`priority` AS `priority`,
  `message_header`.`topic`    AS `topic`,
  `message_header`.`forumid`  AS `forumid`,
  `message_info`.`header`     AS `header`,
  `message_info`.`parent`     AS `parent`,
  `message_info`.`userid`     AS `userid`,
  `message_info`.`timeCreate` AS `timeCreate`,
  `user`.`login`              AS `creator`,
  IFNULL(AVG(`message_rating`.`rating`),0) AS `rating`,
  COUNT(`message_rating`.`rating`) AS `rated`
FROM (((`message_info`
     JOIN `message_header`
       ON ((`message_info`.`header` = `message_header`.`id`)))
    JOIN `user`
      ON ((`message_info`.`userid` = `user`.`id`)))
   JOIN `message_rating`
     ON ((`message_rating`.`infoid` = `message_info`.`id`)))
GROUP BY `message_info`.`id`);

create view `userview` AS (
select
  `user`.`id`              AS `id`,
  `user`.`login`           AS `name`,
  `user`.`name`            AS `userName`,
  `user`.`timeRegistered`  AS `timeRegistered`,
  (CASE WHEN (`session`.`cookie` IS NOT NULL) THEN 1 ELSE 0 END) AS `online`,
  `user`.`deleted`         AS `deleted`,
  `user`.`restrictionType` AS `status`,
  `user`.`banTime`         AS `banTimeExit`,
  `user`.`banCount`        AS `banCount`
FROM (`user`
   LEFT JOIN `session`
     ON ((`session`.`userid` = `user`.`id`))));

create view `userview_super` AS (
select
  `user`.`id`              AS `id`,
  `user`.`login`           AS `name`,
  `user`.`name`            AS `userName`,
  `user`.`email`           AS `email`,
  `user`.`timeRegistered`  AS `timeRegistered`,
  (CASE WHEN (`session`.`cookie` IS NOT NULL) THEN 1 ELSE 0 END) AS `online`,
  `user`.`deleted`         AS `deleted`,
  (CASE WHEN (`user`.`userType` = 'SUPERUSER') THEN 1 ELSE 0 END) AS `superuser`,
  `user`.`restrictionType` AS `status`,
  `user`.`banTime`         AS `banTimeExit`,
  `user`.`banCount`        AS `banCount`
FROM (`user`
   LEFT JOIN `session`
     ON ((`session`.`userid` = `user`.`id`))));
