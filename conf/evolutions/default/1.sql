# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table culture (
  id                        varchar(255) not null,
  title                     longtext,
  detail                    longtext,
  pic                       varchar(255),
  constraint pk_culture primary key (id))
;

create table travel (
  id                        varchar(255) not null,
  title                     longtext,
  detail                    longtext,
  pic                       varchar(255),
  constraint pk_travel primary key (id))
;

create table Users (
  username                  varchar(255) not null,
  password                  varchar(255),
  name                      varchar(255),
  surname                   varchar(255),
  age                       varchar(255),
  sex                       varchar(255),
  address                   varchar(255),
  tel                       varchar(255),
  email                     varchar(255),
  constraint pk_Users primary key (username))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table culture;

drop table travel;

drop table Users;

SET FOREIGN_KEY_CHECKS=1;

