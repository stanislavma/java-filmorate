INSERT INTO GENRE (name)
VALUES ('Comedy'),
       ('Drama'),
       ('Action'),
       ('Romance'),
       ('Thriller');

INSERT INTO AGE_RATING (name)
VALUES ('PG'),
       ('PG-13');

INSERT INTO FILM (AGE_RATING_ID, NAME, RELEASE_DATE, DURATION, DESCRIPTION)
VALUES (1, 'film № 1', '2019-01-01', 170, 'description 1'),
       (1, 'film № 2', '2020-01-01', 180, 'description 2'),
       (1, 'film № 3', '2021-01-01', 190, 'description 3'),
       (2, 'film № 4', '2022-01-01', 200, 'description 4'),
       (2, 'film № 5', '2023-01-01', 210, 'description 5'),
       (2, 'film № 6', '2024-01-01', 220, 'description 6');

INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 5);

insert into APP_USER (LOGIN, NAME, EMAIL, BIRTHDATE)
VALUES ('user_1', 'user_1', 'user_1@gmail.com', '2000-03-03'),
       ('user_2', 'user_2', 'user_2@gmail.com', '2001-03-03'),
       ('user_3', 'user_3', 'user_3@gmail.com', '2002-03-03'),
       ('user_4', 'user_4', 'user_4@gmail.com', '2003-03-03'),
       ('user_5', 'user_5', 'user_5@gmail.com', '2004-03-03'),
       ('user_6', 'user_6', 'user_6@gmail.com', '2005-03-03');

insert into USER_FRIENDSHIP (USER_ID_ONE, USER_ID_TWO, IS_APPROVED)
VALUES (1, 2, false),
       (1, 3, false),
       (1, 4, false),
       (1, 5, false),
       (1, 6, false),

       (2, 1, true),
       (2, 3, true),
       (2, 4, true),
       (2, 5, true),
       (2, 6, true),

       (3, 1, true);

insert into FILM_USER_LIKE (FILM_ID, USER_ID)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),

       (2, 1),
       (3, 1),
       (3, 2);

