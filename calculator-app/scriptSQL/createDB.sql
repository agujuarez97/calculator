-- Script que me permite crear la BD

drop database if exists calculation;

create database calculation;
use calculation;

drop table if exists mycalculation;

create table mycalculation(
	`id` integer,
    `expression` varchar(30),
    `result`     varchar(30),
    constraint pkCalculation primary key (id)
);