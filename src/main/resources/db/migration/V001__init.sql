create table account (
    user_id varchar(255) not null,
    icon binary(2147483647),
    primary key (user_id)
);

create table tweet (
    id binary not null,
    text nvarchar(140),
    timestamp timestamp,
    user_userId varchar(255),
    primary key (id)
);
