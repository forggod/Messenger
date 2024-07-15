create table if not exists users_message (
    id bigserial not null unique ,
    username varchar(120) not null unique,
    email varchar(140) not null unique,
    phone varchar(17) not null unique,
    last_login timestamp not null,
    created_at timestamp not null,
    is_online boolean not null,
    is_deleted boolean not null,
    primary key (id)
);

create table if not exists contacts (
    id bigserial not null unique,
    id_userFirst bigserial not null,
    id_userSecond bigserial not null,
    created_at timestamp not null,
    is_deleted boolean not null,
    primary key (id)
);

create table if not exists chats (
    id bigserial not null unique,
    title varchar(240) not null,
    description varchar(240) not null,
    id_creator bigserial not null,
    created_at timestamp not null,
    is_deleted boolean not null,
    primary key (id, id_creator),
    foreign key (id_creator) references users (id)
);

create table if not exists users_in_chats (
    id bigserial not null unique,
    id_chat bigserial not null,
    id_user bigserial not null,
    created_at timestamp not null,
    is_deleted boolean not null,
    primary key (id),
    foreign key (id_chat) references chats (id),
    foreign key (id_user) references users (id)
);

create table if not exists messages (
    id bigserial not null unique,
    id_chat bigserial not null,
    id_user bigserial not null,
    message varchar(240) not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    is_seen boolean not null,
    is_deleted boolean not null,
    primary key (id),
    foreign key (id_chat) references chats (id),
    foreign key (id_user) references users (id)
);