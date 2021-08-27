drop table if exists rhs_config;
CREATE TABLE `rhs_config`
(
    `id`     int          NOT NULL auto_increment,
    `name`   varchar(100) NOT NULL,
    `config` mediumtext   NOT NULL,
    `status` varchar(10)  NOT NULL DEFAULT 'ACTIVE'
);
