package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    private static final LocalDate BIRTHDATE_OF_CINEMA = LocalDate.of(1895, Month.DECEMBER, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;

    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        validate(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validate(film);
        return filmStorage.update(film);
    }

    public void delete(Film film) {
        validate(film);
        filmStorage.delete(film);
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = findById(filmId);
        checkIfNotNull(film, filmId);
        User user = userService.findById(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    userId));
        }
        film.getLikes().add((long) userId);
        return film;
    }

    public Film removeLike(Integer filmId, Integer userId) {
        Film film = findById(filmId);
        checkIfNotNull(film, filmId);
        User user = userService.findById(userId);
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

    public Film findById(Integer filmId) {
        return filmStorage.findById(filmId).orElseThrow(() -> new FilmNotFoundException(String.format(
                "Фильм %s не найден",
                filmId)));
    }

    private void checkIfNotNull(Film film, Integer filmId) {

        if (film == null) {
            throw new FilmNotFoundException(String.format(
                    "Фильм %s не найден",
                    filmId));
        }

    }

    private void validate(Film film) {

        if (film.getReleaseDate().isBefore(BIRTHDATE_OF_CINEMA)) {
            log.warn("У фильма {} указана дата релиза раньше, чем 28.12.1895", film.toString());
            throw new ValidationException("Указана дата релиза раньше, чем 28.12.1895");
        }
    }

}
