create table if not exists role (
    id serial primary key not null,
    name varchar(15)
)

create table if not exists room (
    id serial primary key not null,
    name varchar(255)
)

create table if not exists message (
    id serial primary key not null,
    message text,
    room_id integer,
    person_id integer
)

insert into room (name) values ('intern')
insert into room (name) values ('junior')
insert into room (name) values ('middle')

insert into message (message, room_id, person_id) values ('Почему не работает сайт www.comcom.com', 1, 5)
insert into message (message, room_id, person_id) values ('Как создать массив в Java', 1, 6)
insert into message (message, room_id, person_id) values ('А arrayList быстрее работает чем массив?', 1, 7)
insert into message (message, room_id, person_id) values ('Кто-нибудь знает как объединить два коммита в Git', 2, 5)
insert into message (message, room_id, person_id) values ('Используй команду rebase -i HEAD~2', 2, 6)
insert into message (message, room_id, person_id) values ('Spring можно настраивать многими способами', 3, 5)