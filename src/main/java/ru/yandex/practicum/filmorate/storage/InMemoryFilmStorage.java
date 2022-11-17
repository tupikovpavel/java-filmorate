package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate BIRTHDATE_OF_CINEMA = LocalDate.of(1895, Month.DECEMBER, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    protected int idCounter = 0;

    @Override
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        validate(film);
        idCounter++;
        film.setId(idCounter);
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        log.debug("Добавленный фильм {}",film.toString());
        return film;
    }

    @Override
    public Film update(Film film) {
        validate(film);
        if (!films.containsKey(film.getId())) {
            log.warn("Список фильмов не содержит {}", film.toString());
            throw new FilmNotFoundException("В списке фильм не найден");
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        films.put(film.getId(), film);
        log.debug("Добавленный фильм {}",film.toString());
        return film;
    }

    @Override
    public void delete(Film film) {
        validate(film);
        if (!films.containsKey(film.getId())) {
            log.warn("Список фильмов не содержит {}", film.toString());
            throw new FilmNotFoundException("В списке фильм не найден");
        }
        films.remove(film);
    }


    private void validate(Film film) {

        if (film.getReleaseDate().isBefore(BIRTHDATE_OF_CINEMA)) {
            log.warn("У фильма {} указана дата релиза раньше, чем 28.12.1895", film.toString());
            throw new ValidationException("Указана дата релиза раньше, чем 28.12.1895");
        }
    }

}
