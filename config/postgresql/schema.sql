create table t_user
(
  id bigserial primary key,
  name varchar(255) not null,
  age integer not null,
  dob timestamp default null,
  null_value integer default null
);