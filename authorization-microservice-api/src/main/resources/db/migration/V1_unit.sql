create table  users(
    id bigserial not null unique ,
    username varchar(120) not null unique ,
    email varchar(140)not null  unique ,
    phone varchar(15) not null  unique ,
    password varchar(120)not null ,
    last_login timestamp not null ,
    created_at timestamp not null,
    is_deleted boolean not null,
    primary key (id)
);

create table roles(
    id bigserial not null unique ,
    role_name varchar(40) not null unique ,
    primary key (id)
);
create table users_roles(
    id_user               bigint not null,
    id_role               bigint not null,
    primary key (id_user, id_role),
    foreign key (id_user) references users (id),
    foreign key (id_role) references roles (id)
);

insert into roles (role_name) values ('ROLE_USER'), ('ROLE_ADMIN');