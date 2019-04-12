create table com_city
(
	id_city integer not null
		constraint com_city_pkey
			primary key,
	name varchar(200),
	postal_code varchar(6)
);

alter table com_city owner to postgres;

create table com_language
(
	id_language integer not null
		constraint com_language_pkey
			primary key,
	name varchar(200)
);

alter table com_language owner to postgres;

create table com_user_data
(
	id_user_data integer not null
		constraint com_user_data_pkey
			primary key,
	address varchar(200),
	birth_date date,
	document_number varchar(20),
	e_mail varchar(80),
	login varchar(15) not null,
	name varchar(30),
	password varchar(80) not null,
	pesel varchar(11),
	registered boolean,
	sex char,
	surname varchar(80),
	id_city integer
		constraint fkrtymcgqf4owkyivcqmsca28d
			references com_city,
	constraint uk9jqq73ba9ea3ya1igo8q467yd
		unique (login, pesel)
);

alter table com_user_data owner to postgres;

create table com_system_properties
(
	id_system_properties integer not null
		constraint com_system_properties_pkey
			primary key,
	font_size integer,
	template_directory varchar(80),
	id_language integer
		constraint fksisti0d1ex2er52xwndrnxuvh
			references com_language,
	id_user_data integer
		constraint fk666hi1kndky5crpi8y1rx3wua
			references com_user_data
);

alter table com_system_properties owner to postgres;

create table lib_client
(
	id_client integer not null
		constraint lib_client_pkey
			primary key
		constraint fk8dui5fp6qj31q7fldhp5qlu11
			references com_user_data
);

alter table lib_client owner to postgres;

create table lib_library_data
(
	id_library_data integer not null
		constraint lib_library_data_pkey
			primary key,
	address varchar(200),
	branch varchar(200),
	name varchar(200),
	id_city integer
		constraint fkofopecup910smifp4wpan7pe8
			references com_city
);

alter table lib_library_data owner to postgres;

create table lib_worker
(
	salary numeric(9,2),
	id_worker integer not null
		constraint lib_worker_pkey
			primary key
		constraint fk211bwkhgpsain51v3der25408
			references com_user_data
);

alter table lib_worker owner to postgres;

create table lib_library_worker
(
	is_accountant boolean,
	id_library_data integer not null
		constraint fk5pumv0ury59j8os4iatfd5ky4
			references lib_library_data,
	id_worker integer not null
		constraint fkjykbsv6er8w2dr762ity6k7e3
			references lib_worker,
	constraint lib_library_worker_pkey
		primary key (id_library_data, id_worker)
);

alter table lib_library_worker owner to postgres;

create sequence seq_com_city;

alter sequence seq_com_city owner to postgres;

create sequence seq_com_language;

alter sequence seq_com_language owner to postgres;

create sequence seq_com_system_properties;

alter sequence seq_com_system_properties owner to postgres;

create sequence seq_com_user_data;

alter sequence seq_com_user_data owner to postgres;

create sequence seq_lib_library_data;

alter sequence seq_lib_library_data owner to postgres;
