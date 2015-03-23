create table t_user
(
  id bigserial primary key,
  name varchar(255) not null,
  age integer not null,
  null_value integer
);