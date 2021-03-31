drop table client;
create table client (
    id int primary key not null serial,
    nom varchar(100) not null,
    numero varchar(10) not null unique,
    cin varchar(12) not null,
    codeSecret varchar(32) not null
);

insert into client values(1, 'Johan', '0344343343', '101221321456', md5('root'));
insert into client values(2, 'Mihaja', '0342547896', '101221321457', md5('root'));

drop table MobileMoney;
create table MobileMoney (
    id int primary key not null,
    idClient int, 
    valeur real not null,
    dateMobileMoney timestamp default current_timestamp not null,
    estValidee boolean default false not null,
    constraint fk_idClient foreign key (idClient) references Client(id) 
);

drop table credit;
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

