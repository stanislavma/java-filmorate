DELETE FROM FILM_GENRE ;
DELETE FROM FILM_USER_LIKE ;
DELETE FROM USER_FRIENDSHIP;

DELETE FROM FILM ;
DELETE FROM APP_USER;

ALTER SEQUENCE FILM_ID_SEQ RESTART WITH 1;
ALTER SEQUENCE USER_ID_SEQ RESTART WITH 1;

DELETE FROM GENRE;
DELETE FROM MPA;

INSERT INTO GENRE (id, name)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

INSERT INTO  MPA (id, name)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');

