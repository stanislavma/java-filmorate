# java-filmorate
Template repository for Filmorate project.

![Database ER diagram](https://github.com/stanislavma/java-filmorate/assets/144703482/70282958-1090-439b-9ba6-9b0e5475cec9)

### Примеры запросов для основных операций
###

#### Добавление пользователя
```postgresql
INSERT INTO user (login, name, email, birthday) VALUES ('user_login', 'user name', 'user@gmail.com', '1990-01-01');
```
---
#### Добавление нового фильма
```postgresql 
INSERT INTO film (name, release_date, duration, description, mpa_id)
VALUES ('Movie name', '2024-04-01', 120, 'Description', 1);
```
---
* Добавление жанра к фильму
```postgresql 
INSERT INTO film_genre (film_id, genre_id) VALUES (1, 5);
```
---
#### Отправка запроса на добавление в друзья
```postgresql
INSERT INTO user_friendship (user_id_one, user_id_two, is_approved) VALUES (1, 2, false);
```
---
#### Пользователь с user_id=2 подтверждает запрос от пользователя с user_id=1 
```postgresql
UPDATE user_friendship SET is_approved = true WHERE user_id_one = 1 AND user_id_two = 2;
```
---
#### Поставить фильму like
```postgresql
INSERT INTO film_user_like (film_id, user_id) VALUES (1, 3);
```