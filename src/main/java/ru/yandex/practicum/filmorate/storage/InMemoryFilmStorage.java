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

    private final Map<Integer, Film> films = new HashMap<>();

    protected int idCounter = 0;

    @Override
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        idCounter++;
        film.setId(idCounter);
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        log.debug("Добавленный фильм {}",film.toString());
        return film;
    }

    @Override
    public Film update(Film film) {
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
    public void delete(Integer filmId) {
        if (!films.containsKey(filmId)) {
            log.warn("Список фильмов не содержит {}", filmId);
            throw new FilmNotFoundException("В списке фильм не найден "+filmId);
        }
        films.remove(filmId);
    }

    @Override
    public Optional<Film> findById(Integer filmId) {
        return Optional.ofNullable(films.get(filmId));
    }
}
