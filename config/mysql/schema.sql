drop table if exists t_user;

create table t_user
(
  id int not null auto_increment,
  name varchar(255) not null,
  age integer not null,
  dob datetime(6) default null,
  null_value integer,

  primary key(id)
);
alter table t_user auto_increment = 0;