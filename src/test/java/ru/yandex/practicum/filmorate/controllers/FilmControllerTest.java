package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    private static final LocalDate RELEASE_DATE_TEST = LocalDate.of(1999, 1, 1);


    @BeforeEach
    void beforeEach() {
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        UserService userService = new UserService(inMemoryUserStorage);
        FilmService filmService = new FilmService(inMemoryFilmStorage, userService);
        filmController = new FilmController(filmService);
    }

    private Film getFilm() {
        return Film.builder()
                .name("Matrix")
                .description("follow the White rabbit")
                .releaseDate(RELEASE_DATE_TEST)
                .duration(160)
                .build();

    }

    @Test
    public void getAll() {
        Film film = getFilm();
        filmController.create(film);
        List<Film> films = filmController.findAll();
        assertEquals(1, films.size());
    }

    @Test
    public void shouldThrowValidationExceptionWhenReleaseDateBefore1895() {
        Film film = getFilm();
        film.setReleaseDate(LocalDate.of(1894, 1, 1));
        assertThrows(ValidationException.class, () -> filmController.create(film), "Указана дата релиза раньше, чем 28.12.1895");
    }

    @Test
    public void shouldThrowValidationExceptionWhenUnknownFilm() {
        Film film = getFilm();
        filmController.create(film);
        Film updatedFilm = getFilm();
        updatedFilm.setId(0);
        assertThrows(ValidationException.class, () -> filmController.update(updatedFilm), "В списке фильм не найден");
    }
}