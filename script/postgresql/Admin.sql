drop table admin;
create table admin(
	id int primary key not null,
	nom varchar(100) not null,
	identifiant varchar(100) not null,
	mdp varchar(32) not null
);

--insert into admin values(1, 'admin test', 'root', md5('root'));

drop table login;
create table login(
	id int primary key not null,
	admin int,
	token varchar(32) not null,
	expiration timestamp not null,
	constraint fk_idAdmin foreign key (admin) references admin(id)
);

insert into admin values(1, 'Admin', 'root',md5('root'));

drop table loginClient;
create table loginClient (
	id int primary key not null,
	client int,
	token varchar(32) not null,
	expiration timestamp not null,
	constraint fk_idClient foreign key (client) references client(id)
);