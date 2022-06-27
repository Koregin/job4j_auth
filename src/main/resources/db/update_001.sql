create table person (
    id serial primary key not null,
    login varchar(2000),
    password varchar(2000),
    employee_id integer
);

create table employee (
    id serial primary key not null,
    name varchar(255),
    lastname varchar(255),
    inn bigint,
    hire_date Timestamp
)

insert into employee (name, lastname, inn, hire_date) values ('Petr', 'Arsentev', 1234567890, '2022-06-27 10:50:13.373000')
insert into employee (name, lastname, inn, hire_date) values ('Ivan', 'Laring', 9876543210, '2022-06-27 10:55:13.373000')
insert into employee (name, lastname, inn, hire_date) values ('Anton', 'Gusev', 9823749283, '2022-06-27 10:59:13.373000')

insert into person (login, password, employee_id) values ('parsentev', '123', 1);
insert into person (login, password, employee_id) values ('parsentev2', '123', 1);
insert into person (login, password, employee_id) values ('ban', '123', 3);
insert into person (login, password, employee_id) values ('ivan', '123', 2);