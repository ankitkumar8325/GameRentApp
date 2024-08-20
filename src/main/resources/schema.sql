create table if not exists GAMES (
    title varchar(255) not null,
    studio varchar(255) not null,

    primary key (title, studio)
);

create table if not exists USERS (
    id varchar(255) not null,
    username varchar(255) not null,
    name varchar(255) not null,

    primary key (id)
);

create table if not exists GENRES (
    id varchar(255) not null,
    genreName varchar(255) not null,
    genreDetail varchar(255) not null,

    primary key (id)
);

create table if not exists GENRES_GAME_RELATION (
    genreId varchar(255) not null,
    gameTitle varchar(255) not null,
    gameStudio varchar(255) not null,

    primary key (genreId, gameTitle, gameStudio),

    foreign key (genreId) references GENRES(id),
    foreign key (gameTitle, gameStudio) references GAMES(title, studio)
);

create table if not exists USER_GAME_RELATION (
    userId varchar(255) not null,
    gameTitle varchar(255) not null,
    gameStudio varchar(255) not null,

    primary key (userId, gameTitle, gameStudio),

    foreign key (userId) references USERS(id),
    foreign key (gameTitle, gameStudio) references GAMES(title, studio)
);