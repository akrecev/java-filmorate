MERGE INTO GENRES (GENRE_ID, GENRE_NAME)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

MERGE INTO MPA (MPA_ID, MPA_NAME)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');

INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTH_DAY)
VALUES ('a@a.com', 'user1', 'userName1', '2000-01-01'),
       ('b@b.com', 'user2', 'userName2', '2000-01-02');

INSERT INTO FILMS (FILM_NAME, DESCRIPTION, DURATION, RELEASE_DATE, RATE, MPA_ID)
VALUES ('L’Arrivée d’un train en gare de la Ciotat', 'Train arriving at La Ciotat station', 1, '1895-12-28', 0, 3),
       ('Terminator 2: Judgment Day', 'Hasta la vista, baby', 137, '1991-12-25', 0, 4);