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

create table IF NOT EXISTS DIRECTORS
(
    DIRECTOR_ID   INTEGER auto_increment,
    DIRECTOR_NAME CHARACTER VARYING(200) not null,
    constraint "DIRECTORS_pk"
        primary key (DIRECTOR_ID)
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

create table IF NOT EXISTS FILM_DIRECTORS
(
    FILM_ID  INTEGER not null,
    DIRECTOR_ID INTEGER not null,
    constraint "FILM_DIRECTORS_pk"
        primary key (FILM_ID, DIRECTOR_ID),
    constraint "FILM_DIRECTORS_FILMS_null_fk"
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint "FILM_DIRECTORS_DIRECTORS_null_fk"
        foreign key (DIRECTOR_ID) references DIRECTORS
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

create table IF NOT EXISTS REVIEWS
(
    review_id  INTEGER auto_increment
        primary key,
    is_positive BOOLEAN not null,
    content    CHARACTER VARYING(5000),
    user_id    INTEGER not null,
    film_id    INTEGER not null,
    useful     INTEGER,
    constraint REVIEWS_FILMS_FILM_ID_FK
        foreign key (film_id) references FILMS
            on delete cascade,
    constraint REVIEWS_USERS_USER_ID_FK
        foreign key (user_id) references USERS
            on delete cascade
);

create table IF NOT EXISTS REVIEW_LIKES
(
    user_id   INTEGER not null,
    review_id INTEGER not null,
    constraint REVIEW_LIKES_PK
        primary key (user_id, review_id),
    constraint REVIEW_LIKES_REVIEWS_REVIEW_ID_FK
        foreign key (review_id) references REVIEWS
            on delete cascade,
    constraint REVIEW_LIKES_USERS_USER_ID_FK
        foreign key (user_id) references USERS
            on delete cascade
);


create table IF NOT EXISTS REVIEW_DISLIKES
(
    user_id   INTEGER not null,
    review_id INTEGER not null,
    constraint REVIEW_DISLIKES_PK
        primary key (user_id, review_id),
    constraint REVIEW_DISLIKES_REVIEWS_REVIEW_ID_FK
        foreign key (review_id) references REVIEWS
            on delete cascade,
    constraint REVIEW_DISLIKES_USERS_USER_ID_FK
        foreign key (user_id) references USERS
            on delete cascade
);

CREATE TABLE IF NOT EXISTS USER_ACTIONS
(
    event_id INTEGER AUTO_INCREMENT,
    user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    event_type VARCHAR(10) NOT NULL,
    operation VARCHAR(10) NOT NULL,
    entity_id INTEGER NOT NULL,
    timestamp BIGINT NOT NULL,
    EVENT_ID_PK INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY
);