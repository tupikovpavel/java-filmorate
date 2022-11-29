package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    public void delete(Integer filmId) {
        filmStorage.delete(filmId);
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = findById(filmId);
        User user = userService.findById(userId);
        film.getLikes().add((long) userId);
        return film;
    }

    public Film removeLike(Integer filmId, Integer userId) {
        Film film = findById(filmId);
        User user = userService.findById(userId);
        film.getLikes().remove((long) userId);
        return film;
    }

    public List<Film> findAll(Integer count) {
        return filmStorage.findAll().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findById(Integer filmId) {
        return filmStorage.findById(filmId).orElseThrow(() -> new FilmNotFoundException(String.format(
                "Фильм %s не найден",
                filmId)));
    }

    private int compare(Film p0, Film p1) {
        return p1.getLikes().size() - p0.getLikes().size();
    }

}
