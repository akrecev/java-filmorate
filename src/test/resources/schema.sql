create table if not exists MPA
(
    MPA_ID   INTEGER auto_increment,
    MPA_NAME CHARACTER VARYING(5) not null,
    constraint "MPA_pk"
        primary key (MPA_ID)
);

create table if not exists GENRES
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(255) not null,
    constraint "GENRES_pk"
        primary key (GENRE_ID)
);

create table if not exists FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    CHARACTER VARYING(255) not null,
    DESCRIPTION  CHARACTER VARYING(255) not null,
    DURATION     INTEGER                not null,
    RELEASE_DATE DATE                   not null,
    MPA_ID       INTEGER,
    RATE         INTEGER,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);

create table if not exists USERS
(
    USER_ID   INTEGER auto_increment,
    EMAIL     CHARACTER VARYING(255) not null
        unique,
    LOGIN     CHARACTER VARYING(255) not null
        unique,
    USER_NAME CHARACTER VARYING(255) not null,
    BIRTH_DAY DATE,
    constraint USERS_PK
        primary key (USER_ID)
);

create table if not exists FILM_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint "FILM_GENRES_pk"
        primary key (FILM_ID, GENRE_ID),
    constraint "FILM_GENRES_FILMS_null_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "FILM_GENRES_GENRES_null_fk"
        foreign key (GENRE_ID) references GENRES
);

create table if not exists FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint FRIENDS_PK
        primary key (USER_ID, FRIEND_ID),
    constraint USER_FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS,
    constraint USER_FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
);

create table if not exists LIKES
(
    USER_ID INTEGER not null,
    FILM_ID INTEGER not null,
    constraint USER_LIKES_PK
        primary key (USER_ID, FILM_ID),
    constraint USER_LIKES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint USER_LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);

create table if not exists REVIEWS
(
    REVIEW_ID INTEGER auto_increment,
    POSITIVE  BOOLEAN                 not null,
    HEADER    CHARACTER VARYING(255)  not null,
    CONTENT   CHARACTER VARYING(5000) not null,
    FILM_ID   INTEGER                 not null,
    USER_ID   INTEGER                 not null,
    RATE      INTEGER,
    constraint REVIEWS_PK
        primary key (REVIEW_ID),
    constraint REVIEWS_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint REVIEWS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);

create table if not exists REVIEW_DISLIKES
(
    USER_ID   INTEGER not null,
    REVIEW_ID INTEGER not null,
    primary key (USER_ID, REVIEW_ID),
    constraint USER_REVIEW_DISLIKES_REVIEWS_REVIEW_ID_FK
        foreign key (REVIEW_ID) references REVIEWS
            on delete cascade,
    constraint USER_REVIEW_DISLIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);

create table if not exists REVIEW_LIKES
(
    USER_ID   INTEGER not null,
    REVIEW_ID INTEGER not null,
    constraint USER_REVIEW_LIKES_PK
        primary key (USER_ID, REVIEW_ID),
    constraint USER_REVIEW_LIKES_REVIEWS_REVIEW_ID_FK
        foreign key (REVIEW_ID) references REVIEWS
            on delete cascade,
    constraint USER_REVIEW_LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);
