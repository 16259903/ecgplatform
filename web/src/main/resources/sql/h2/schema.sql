drop table if exists user;

create table user (
	id bigint generated by default as identity,
	code varchar(20) not null,
	username varchar(20) not null unique,
	name varchar(20) not null,
	password varchar(100) not null,
	roles varchar(100),
    gender int not null,
	type varchar(10) not null,
	birthday timestamp not null,
	address varchar(255),
	stature double,
	weight  double,
	fn_place varchar(20),
	city     varchar(20),
	tel      varchar(20),
	em_contact1 varchar(20),
	em_contact1_tel varchar(20),
	em_contact2 varchar(20),
	em_contact2_tel varchar(20),
	bad_habits varchar(30),
	anamnesis varchar(30),
	created_date timestamp,
	last_updated timestamp,
	remark varchar(255),
	title varchar(20),
	mobile_num varchar(30),
	is_free int,
	version int,
	primary key (id)
);

drop table if exists employee;

create table employee (
    id bigint generated by default as identity,
    type varchar(20),
    name varchar(20) not null ,
    username varchar(20) not null unique,
    password varchar(100) not null,
    roles varchar(100),
    status varchar(10) not null,
    enabled int not null,
    dismissed int not null,
    gender int not null,
    expire timestamp,
    birthday timestamp,
    id_card varchar(20),
    mobile varchar(11),
    title varchar(20),
    hospital varchar(100),
    created_date timestamp,
    last_updated timestamp,
    email varchar(64),
    version int,
    primary key(id)
);

drop table if exists expert_operator;

create table expert_operator (
    expert_id bigint not null ,
    operator_id bigint not null
);