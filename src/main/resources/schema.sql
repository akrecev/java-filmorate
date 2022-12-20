create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(50) not null,
    constraint "GENRES_pk"
        primary key (GENRE_ID)
);

create table IF NOT EXISTS MPA
(
    MPA_ID   INTEGER auto_increment,
    MPA_NAME CHARACTER VARYING(5) not null,
    constraint "MPA_pk"
        primary key (MPA_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING(150) not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    DURATION     INTEGER                not null,
    RELEASE_DATE DATE                   not null,
    RATE         INTEGER,
    MPA_ID       INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table IF NOT EXISTS FILM_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint "FILM_GENRES_pk"
        primary key (FILM_ID, GENRE_ID),
    constraint "FILM_GENRES_FILMS_null_fk"
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint "FILM_GENRES_GENRES_null_fk"
        foreign key (GENRE_ID) references GENRES
            on delete cascade
);

create table IF NOT EXISTS USERS
(
    USER_ID   INTEGER auto_increment,
    EMAIL     CHARACTER VARYING(255) not null
        unique,
    LOGIN     CHARACTER VARYING(255) not null
        unique,
    USER_NAME CHARACTER VARYING(50)  not null,
    BIRTH_DAY DATE,
    constraint USERS_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint FRIENDS_PK
        primary key (USER_ID, FRIEND_ID),
    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade,
    constraint FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
            on delete cascade
);

create table IF NOT EXISTS LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint "LIKES_pk"
        primary key (FILM_ID, USER_ID),
    constraint LIKES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);