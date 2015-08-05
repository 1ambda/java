DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` varchar(10) NOT NULL,
  `name` varchar(20) NOT NULL,
  `password` varchar(10) NOT NULL,
  `level` int(11) NOT NULL,
  `login` int(11) NOT NULL,
  `recommend` int(11) NOT NULL,
  `email` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
);

