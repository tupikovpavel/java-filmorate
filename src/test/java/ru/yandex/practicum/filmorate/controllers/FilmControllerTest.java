package ru.yandex.practicum.filmorate.controllers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    private static final LocalDate RELEASE_DATE_TEST = LocalDate.of(1999, 1, 1);

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
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
    public void shouldThrowValidationExceptionWhenNameEmpty() {
        Film film = getFilm();
        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.create(film), "Не указано название фильма");
    }

    @Test
    public void shouldThrowValidationExceptionWhenDescriptionLongerThan200Symbols() {
        Film film = getFilm();
        StringBuilder sb = new StringBuilder();
        sb.append("follow the White rabbit follow the White rabbit follow the White rabbit follow the White rabbit")
                .append("follow the White rabbit follow the White rabbit follow the White rabbit follow the White rabbit")
                .append("follow the White rabbit follow the White rabbit follow the White rabbit follow the White rabbit")
                .append("follow the White rabbit follow the White rabbit follow the White rabbit follow the White rabbit")
                .append("follow the White rabbit follow the White rabbit follow the White rabbit follow the White rabbit")
                .append("follow the White rabbit follow the White rabbit follow the White rabbit follow the White rabbit");
        film.setDescription(sb.toString());
        assertThrows(ValidationException.class, () -> filmController.create(film), "Описание фильма содержит более 200 символов");
    }

    @Test
    public void shouldThrowValidationExceptionWhenReleaseDateBefore1895() {
        Film film = getFilm();
        film.setReleaseDate(LocalDate.of(1894, 1, 1));
        assertThrows(ValidationException.class, () -> filmController.create(film), "Указана дата релиза раньше, чем 28.12.1895");
    }

    @Test
    public void shouldThrowValidationExceptionWhenDurationNegative() {
        Film film = getFilm();
        film.setDuration(-10);
        assertThrows(ValidationException.class, () -> filmController.create(film), "Продолжительность фильма должна быть положительная");
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