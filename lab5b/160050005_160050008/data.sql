insert into users values ('001', 'a', '001');
insert into users values ('002', 'b', '002');
insert into users values ('003', 'c', '003');
insert into users values ('004', 'd', '004');
insert into users values ('005', 'e', '005');

insert into conversations values ('001', '002', nextval('serial'));
insert into conversations values ('002', '003', nextval('serial'));

insert into password (select uid, phone from users);