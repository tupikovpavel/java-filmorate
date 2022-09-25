package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();

    protected int idCounter = 0;

    @GetMapping()
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film create(@RequestBody Film film) {
        validate(film);
        idCounter++;
        film.setId(idCounter);
        films.put(film.getId(), film);
        log.debug("Добавленный фильм {}",film.toString());
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Список фильмов не содержит {}", film.toString());
            throw new ValidationException("В списке фильм не найден");
        }
        validate(film);
        films.put(film.getId(), film);
        log.debug("Добавленный фильм {}",film.toString());
        return film;
    }

    private void validate(Film film) {
        if ((film.getName() == null) || (film.getName().isEmpty())) {
            log.warn("Не указано название фильма {}", film.toString());
            throw new ValidationException("Не указано название фильма");
        }
        if (film.getDescription().length()>200) {
            log.warn("Описание фильма {} содержит более 200 символов", film.toString());
            throw new ValidationException("Описание фильма содержит более 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.warn("У фильма {} указана дата релиза раньше, чем 28.12.1895", film.toString());
            throw new ValidationException("Указана дата релиза раньше, чем 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            log.warn("У фильма {} указана некорректная продолжительность", film.toString());
            throw new ValidationException("Продолжительность фильма должна быть положительная");
        }
    }

}
