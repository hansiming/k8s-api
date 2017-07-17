
CREATE DATABASE `k8s` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

create table namespace (id int primary key auto_increment, user_name varchar(255) not null, namespace_name varchar(255) not null)  DEFAULT CHARSET=utf8;