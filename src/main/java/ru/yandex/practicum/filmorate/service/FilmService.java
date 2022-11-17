package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;

    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(Integer filmId, Integer userId) {
        if (filmId == null) {
            throw new FilmNotFoundException(String.format(
                    "Фильм %s не найден",
                    filmId));
        }
        if (userId == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    userId));
        }
        Film film = findFilmById(filmId);
        validate(film, filmId);
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    userId));
        }
        film.getLikes().add((long) userId);
        return film;
    }

    public Film removeLike(Integer filmId, Integer userId) {
        if (filmId == null) {
            throw new FilmNotFoundException(String.format(
                    "Фильм %s не найден",
                    filmId));
        }
        if (userId == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    userId));
        }
        Film film = findFilmById(filmId);
        validate(film, filmId);
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    userId));
        }
        film.getLikes().remove((long) userId);
        return film;
    }

    public List<Film> findAll(Integer count) {
        return filmStorage.findAll().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film p0, Film p1) {
        return p1.getLikes().size() - p0.getLikes().size();
    }

    public Film findFilmById(Integer filmId) {
        return filmStorage.findAll().stream().filter(p -> p.getId().equals(filmId))
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException(String.format("Фильм № %d не найден", filmId)));
    }

    private void validate(Film film, Integer filmId) {

        if (film == null) {
            throw new FilmNotFoundException(String.format(
                    "Фильм %s не найден",
                    filmId));
        }

    }

}
