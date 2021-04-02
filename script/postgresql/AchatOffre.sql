create table solde (
	idClient int UNIQUE,
	credit real,
	mobilemoney real,
	foreign key (idClient) references client(id)
);

create sequence idOffre;

insert into solde values(1, 5000, 15000);
insert into solde  values(2, 0, 25000);
/* drop table soldeOffre;
 drop table achatOffre;
 * */

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

--alter table soldeOffre drop column idClient, idOffre;
--select cast(date_part('month', timestamp '2021-03-23') as varchar(10) );

--create or replace view statMois as select idOffre, date_part('month', debut) as mois, count(*) as somme from achatOffre group by month, idOffre;

create or replace view v_joursMois as select distinct(debut) as jour, cast(date_part('month', debut) as varchar(3)) as mois, cast(date_part('year', debut) as varchar(5)) as annee from achatOffre;

--create or replace view v_

create or replace view v_offreStat as select distinct (idOffre) as idOffre from achatOffre;

create or replace view v_stat as select v_joursMois.*, idOffre, 0 as somme from v_joursMois join v_offreStat on 1=1;

create or replace view v_statOffre as select v_stat.idOffre ,jour, GREATEST(count(achatOffre.*), somme) as somme, mois, annee from achatOffre right join v_stat on debut = jour and v_stat.idOffre = achatOffre.idOffre  group by jour, v_stat.idOffre, mois, annee, somme;




create table typeOffre(
	id int primary key,
	types varchar(20)
);

insert into typeOffre values (1, 'Mora'), (2, 'Special'), (3, 'Internet'), (4, 'First');
























