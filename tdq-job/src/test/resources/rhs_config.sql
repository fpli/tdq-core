drop table if exists rhs_config;
CREATE TABLE `rhs_config`
(
    `config` mediumtext  NOT NULL,
    `status` varchar(10) NOT NULL DEFAULT 'ACTIVE'
);
