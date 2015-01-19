
    alter table CUSTOMER 
        drop constraint FK_rrxtpgq1uomd6b2m4gor56bhi if exists;

    alter table PIZZA 
        drop constraint FK_2b0sbnjd7cbmswul8w1ebidck if exists;

    alter table PIZZA_INGREDIENT 
        drop constraint FK_voohnv6jngptmfl8fglhmuhd if exists;

    alter table PIZZA_INGREDIENT 
        drop constraint FK_s2njcbcuqdoxe196yyrsp5ep7 if exists;

    alter table PRODUCT 
        drop constraint FK_l12xpmcmyo4n2g1hpcqj2vmt9 if exists;

    alter table PURCHASE 
        drop constraint FK_hwwrov2ynrakvodth3mmvlve6 if exists;

    drop table ADDRESS if exists;

    drop table CUSTOMER if exists;

    drop table INGREDIENT if exists;

    drop table PIZZA if exists;

    drop table PIZZA_INGREDIENT if exists;

    drop table PRODUCT if exists;

    drop table PURCHASE if exists;

    create table ADDRESS (
        id bigint generated by default as identity,
        city varchar(255) not null,
        country varchar(255) not null,
        street varchar(255) not null,
        zip varchar(255) not null,
        primary key (id)
    );

    create table CUSTOMER (
        id bigint generated by default as identity,
        firstName varchar(255) not null,
        surName varchar(255) not null,
        ADDRESS_ID bigint not null,
        primary key (id)
    );

    create table INGREDIENT (
        id bigint not null,
        cost decimal(19,2) not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table PIZZA (
        ID bigint not null,
        primary key (ID)
    );

    create table PIZZA_INGREDIENT (
        PIZZA_ID bigint not null,
        ingredients_id bigint not null
    );

    create table PRODUCT (
        id bigint generated by default as identity,
        PURCHASE_ID bigint not null,
        primary key (id)
    );

    create table PURCHASE (
        id bigint generated by default as identity,
        billed boolean not null,
        delivered boolean not null,
        prepared boolean not null,
        CUSTOMER_ID bigint not null,
        primary key (id)
    );

    alter table CUSTOMER 
        add constraint FK_rrxtpgq1uomd6b2m4gor56bhi 
        foreign key (ADDRESS_ID) 
        references ADDRESS;

    alter table PIZZA 
        add constraint FK_2b0sbnjd7cbmswul8w1ebidck 
        foreign key (ID) 
        references PRODUCT;

    alter table PIZZA_INGREDIENT 
        add constraint FK_voohnv6jngptmfl8fglhmuhd 
        foreign key (ingredients_id) 
        references INGREDIENT;

    alter table PIZZA_INGREDIENT 
        add constraint FK_s2njcbcuqdoxe196yyrsp5ep7 
        foreign key (PIZZA_ID) 
        references PIZZA;

    alter table PRODUCT 
        add constraint FK_l12xpmcmyo4n2g1hpcqj2vmt9 
        foreign key (PURCHASE_ID) 
        references PURCHASE;

    alter table PURCHASE 
        add constraint FK_hwwrov2ynrakvodth3mmvlve6 
        foreign key (CUSTOMER_ID) 
        references CUSTOMER;