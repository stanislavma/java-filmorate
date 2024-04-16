-- truncate table USER_FRIENDSHIP;
-- truncate table FILM_USER_LIKE;
-- truncate table FILM_GENRE;
--
delete from FILM_GENRE where id > 0;
delete from FILM where id > 0;

ALTER SEQUENCE FILM_ID_SEQ RESTART WITH 1;

-- delete from APP_USER where id > 0;
delete from GENRE where id > 0;
delete from  MPA where id > 0;


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

-- INSERT INTO FILM (id,  MPA, NAME, RELEASE_DATE, DURATION, DESCRIPTION)
-- VALUES (1, 1, 'film № 1', '2019-01-01', 170, 'description 1'),
--        (2, 1, 'film № 2', '2020-01-01', 180, 'description 2'),
--        (3, 1, 'film № 3', '2021-01-01', 190, 'description 3'),
--        (4, 2, 'film № 4', '2022-01-01', 200, 'description 4'),
--        (5, 2, 'film № 5', '2023-01-01', 210, 'description 5'),
--        (6, 2, 'film № 6', '2024-01-01', 220, 'description 6');
--
-- INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID)
-- VALUES (1, 1),
--        (2, 2),
--        (3, 3),
--        (4, 4),
--        (5, 5),
--        (6, 5);
--
-- insert into APP_USER (id, LOGIN, NAME, EMAIL, BIRTHDATE)
-- VALUES (1, 'user_1', 'user_1', 'user_1@gmail.com', '2000-03-03'),
--        (2, 'user_2', 'user_2', 'user_2@gmail.com', '2001-03-03'),
--        (3, 'user_3', 'user_3', 'user_3@gmail.com', '2002-03-03'),
--        (4, 'user_4', 'user_4', 'user_4@gmail.com', '2003-03-03');
--
-- insert into USER_FRIENDSHIP (USER_ID_ONE, USER_ID_TWO, IS_APPROVED)
-- VALUES (1, 2, false),
--        (1, 3, false),
--        (1, 4, false),
--
--        (2, 1, true),
--        (2, 3, true);
--
-- insert into FILM_USER_LIKE (FILM_ID, USER_ID)
-- VALUES (1, 1),
--        (1, 2),
--        (1, 3),
--        (1, 4),
--
--        (2, 1),
--        (3, 1),
--        (3, 2);
--
