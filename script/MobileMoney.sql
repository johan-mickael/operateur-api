drop table client;
create table client (
    id int primary key not null,
    nom varchar(100) not null,
    numero varchar(10) not null,
    cin varchar(12) not null,
    codeSecret varchar(32) not null
);

insert into client values(1, 'Client 1', '0344343343', '101221321456', md5('root'));

drop table MobileMoney;
create table MobileMoney (
    id int primary key not null,
    idClient int, 
    valeur real not null,
    dateMobileMoney timestamp default current_timestamp not null,
    estValidee boolean default false not null,
    constraint fk_idClient foreign key (idClient) references Client(id) 
);

insert into MobileMoney values(1, 1, 10000, default, default);
insert into MobileMoney values(2, 1, 5000.5, default, default);

create view 