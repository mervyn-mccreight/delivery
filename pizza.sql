
    alter table CUSTOMERS 
        drop constraint FK_mrnmmjaeufr92dj7w7yiuys8r if exists;

    alter table ORDERS 
        drop constraint FK_57wwsm6wqqkcr1amp4dtsk5bs if exists;

    alter table PIZZAS 
        drop constraint FK_c8l5tg8gjkx4fb6bsscfu5evb if exists;

    alter table PIZZAS_INGREDIENTS 
        drop constraint FK_6j0kbrp7p88rujv1xunlh2dgu if exists;

    alter table PIZZAS_INGREDIENTS 
        drop constraint FK_mvq0489ltsqgrpfiqr8a4jcau if exists;

    alter table PRODUCTS 
        drop constraint FK_4btskvr0x6lp1ggyxq0s78df3 if exists;

    drop table ADDRESSES if exists;

    drop table CUSTOMERS if exists;

    drop table INGREDIENTS if exists;

    drop table ORDERS if exists;

    drop table PIZZAS if exists;

    drop table PIZZAS_INGREDIENTS if exists;

    drop table PRODUCTS if exists;

    drop sequence ADDRESS_SEQ;

    drop sequence CUSTOMER_SEQ;

    drop sequence ORDER_SEQ;

    drop sequence PRODUCT_SEQ;

    create table ADDRESSES (
        id bigint not null,
        city varchar(255) not null,
        country varchar(255) not null,
        street varchar(255) not null,
        zip varchar(255) not null,
        primary key (id)
    );

    create table CUSTOMERS (
        id bigint not null,
        firstName varchar(255) not null,
        surName varchar(255) not null,
        ADDRESS_ID bigint,
        primary key (id)
    );

    create table INGREDIENTS (
        id bigint not null,
        cost decimal(19,2) not null,
        name varchar(255) not null,
        primary key (id)
    );

    create table ORDERS (
        id bigint not null,
        billed boolean not null,
        delivered boolean not null,
        prepared boolean not null,
        CUSTOMER_ID bigint,
        primary key (id)
    );

    create table PIZZAS (
        ID bigint not null,
        primary key (ID)
    );

    create table PIZZAS_INGREDIENTS (
        PIZZAS_ID bigint not null,
        ingredients_id bigint not null
    );

    create table PRODUCTS (
        id bigint not null,
        ORDER_ID bigint,
        primary key (id)
    );

    alter table CUSTOMERS 
        add constraint FK_mrnmmjaeufr92dj7w7yiuys8r 
        foreign key (ADDRESS_ID) 
        references ADDRESSES;

    alter table ORDERS 
        add constraint FK_57wwsm6wqqkcr1amp4dtsk5bs 
        foreign key (CUSTOMER_ID) 
        references CUSTOMERS;

    alter table PIZZAS 
        add constraint FK_c8l5tg8gjkx4fb6bsscfu5evb 
        foreign key (ID) 
        references PRODUCTS;

    alter table PIZZAS_INGREDIENTS 
        add constraint FK_6j0kbrp7p88rujv1xunlh2dgu 
        foreign key (ingredients_id) 
        references INGREDIENTS;

    alter table PIZZAS_INGREDIENTS 
        add constraint FK_mvq0489ltsqgrpfiqr8a4jcau 
        foreign key (PIZZAS_ID) 
        references PIZZAS;

    alter table PRODUCTS 
        add constraint FK_4btskvr0x6lp1ggyxq0s78df3 
        foreign key (ORDER_ID) 
        references ORDERS;

    create sequence ADDRESS_SEQ;

    create sequence CUSTOMER_SEQ;

    create sequence ORDER_SEQ;

    create sequence PRODUCT_SEQ;
