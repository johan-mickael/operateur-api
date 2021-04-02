CREATE DATABASE s5final;
CREATE ROLE s5Role LOGIN PASSWORD 'root';
ALTER DATABASE s5final OWNER TO s5Role;
-----------------------------------------------------------------------


create table operateurs (
	id int primary key,
	nom varchar(15)
);

create table produits (
	id int primary key,
	nom varchar(15)
);

create table details (
	id int primary key,
	idProduit int,
	idOperateur int,
	foreign key (idProduit) references produits (id),
	foreign key (idOperateur) references operateurs (id)
);

create table tarifs (
	id int primary key,
	idDetails int,
	prix real,
	foreign key (idDetails) references details (id)
);

create or replace view v_tarif as select tarifs.*, produits.nom as produits, operateurs.nom as operateurs from tarifs, produits, details left join operateurs on details.idOperateur  = operateurs.id where tarifs.idDetails = details.id and details.idProduit = produits.id;

insert into operateurs values (1, 'OmG'), (2, 'Autres'), (3, 'International');

insert into produits values (1, 'Appel');
insert into produits values (2, 'Message');
insert into produits values (3, 'Internet');

insert into details values (1, 1, 1), (2, 2, 1), (3, 1, 2), (4, 2, 2), (5, 1, 3), (6, 2, 3);
insert into details values (7, 3, null);

insert into tarifs values (1, 1, 0.8), (2, 2, 80), (3, 3, 1.2), (4, 4, 120), (5, 5, 5), (6, 6, 210), (7, 7, 120);
