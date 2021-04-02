/*CREATE DATABASE s5final;
CREATE ROLE s5Role LOGIN PASSWORD 'root';
ALTER DATABASE s5final OWNER TO s5Role;

\c s5final;*/

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
insert into produits values (4, 'Facebook');
insert into produits values (5, 'Instagram');
insert into details values (1, 1, 1), (2, 2, 1), (3, 1, 2), (4, 2, 2), (5, 1, 3), (6, 2, 3);
insert into details values (7, 3, null);
insert into tarifs values (1, 1, 0.8), (2, 2, 80), (3, 3, 1.2), (4, 4, 120), (5, 5, 5), (6, 6, 210), (7, 7, 120);

create table client (
    id int primary key not null,
    nom varchar(100) not null,
    numero varchar(10) not null unique,
    cin varchar(12) not null,
    codeSecret varchar(32) not null
);

insert into client values(1, 'Johan', '0344343343', '101221321456', md5('root'));
insert into client values(2, 'Mihaja', '0342547896', '101221321457', md5('root'));

create table MobileMoney (
    id int primary key not null,
    idClient int, 
    valeur real not null,
    dateMobileMoney timestamp default current_timestamp not null,
    estValidee boolean default false not null,
    constraint fk_idClient foreign key (idClient) references Client(id) 
);

create table credit (
    id int primary key not null,
    idClient int,
    valeur real not null,
    dateCredit timestamp default current_timestamp not null,
    constraint fk_idClient_credit foreign key (idClient) references client(id)
);

insert into credit values (1, 1, 1000, current_timestamp);
insert into credit values (2, 1, 1500, current_timestamp);
insert into MobileMoney values(1, 1, 10000, default, default);
insert into MobileMoney values(2, 1, 5000.5, default, default);

create view v_depot as select 
m.id, c.id idclient, c.nom, c.numero, c.cin, m.valeur, m.dateMobileMoney, m.estValidee 
from MobileMoney m join client c on c.id = m.idClient;

create view v_depot_non_valide as select * from v_depot where estValidee = false;

create view v_depot_valide as select * from v_depot where estValidee = true;

create view v_solde_mobile_money as
select c.id, coalesce(valeur, 0) valeur from client c left join v_depot_valide d on c.id = d.idClient;

create view v_somme_solde_mobile_money as
select id, sum(valeur) from v_solde_mobile_money
group by id;

create view v_solde_credit as
select c.id, coalesce(cr.valeur, 0) valeur from client c left join credit cr
on c.id = cr.idClient;

create view v_somme_solde_credit as
select id, sum(valeur) from v_solde_credit
group by id;

create table admin(
	id int primary key not null,
	nom varchar(100) not null,
	identifiant varchar(100) not null,
	mdp varchar(32) not null
);
insert into admin values(1, 'Admin', 'root',md5('root'));

create table login(
	id int primary key not null,
	admin int,
	token varchar(32) not null,
	expiration timestamp not null,
	constraint fk_idAdmin foreign key (admin) references admin(id)
);

create table loginClient (
	id int primary key not null,
	client int,
	token varchar(32) not null,
	expiration timestamp not null,
	foreign key (client) references client(id)
);
create table solde (
	idClient int UNIQUE,
	credit real,
	mobilemoney real,
	foreign key (idClient) references client(id)
);
create sequence idOffre start with 5 increment by 1;

insert into solde values(1, 5000, 15000);
insert into solde  values(2, 0, 25000);

create sequence achat;
create table achatOffre (
	id int primary key,
	idClient int,
	idOffre varchar(30),
	methode varchar(30),
	debut date,
	fin date,
	foreign key (idClient) references client(id)
);

create table soldeOffre (
	idAchat int,
	produit int,
	valeur int,
	unite varchar(8),
	operateur real,
	autres real,
	international real,
	delai date,
	foreign key (produit) references produits(id),
	foreign key (idAchat) references achatOffre(id)
);

create or replace view v_joursMois as select distinct(debut) as jour, cast(date_part('month', debut) as varchar(3)) as mois, cast(date_part('year', debut) as varchar(5)) as annee from achatOffre;
create or replace view v_offreStat as select distinct (idOffre) as idOffre from achatOffre;
create or replace view v_stat as select v_joursMois.*, idOffre, 0 as somme from v_joursMois join v_offreStat on 1=1;
create or replace view v_statOffre as select v_stat.idOffre ,jour, GREATEST(count(achatOffre.*), somme) as somme, mois, annee from achatOffre right join v_stat on debut = jour and v_stat.idOffre = achatOffre.idOffre  group by jour, v_stat.idOffre, mois, annee, somme;

create table typeOffre(
	id int primary key,
	types varchar(20)
);

insert into typeOffre values (1, 'Mora'), (2, 'Special'), (3, 'Internet'), (4, 'First');
