create table rules_rhs_config
(
	id int auto_increment primary key,
	config text not null,
	reference_id int null,
	status varchar(10) not null default 'ACTIVE',
	crd_user varchar(10) default 'admin' null,
	crd_date datetime null,
	upd_user varchar(10) default 'admin' null,
	upd_date datetime null
);