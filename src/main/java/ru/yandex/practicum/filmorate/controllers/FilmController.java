package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {

    private final FilmService filmService;

    private static final LocalDate BIRTHDATE_OF_CINEMA = LocalDate.of(1895, Month.DECEMBER, 28);

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        return filmService.create(film);
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        validate(film);
        return filmService.update(film);
    }


    @GetMapping("/{id}")
    public Film findById(@PathVariable("id") Integer id) {
        return filmService.findById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> findAll(@RequestParam(value = "count", defaultValue = "10", required = false) @Positive Integer count) {
        return filmService.findAll(count);
    }

    private void validate(Film film) {

        if (film.getReleaseDate().isBefore(BIRTHDATE_OF_CINEMA)) {
            log.warn("У фильма {} указана дата релиза раньше, чем 28.12.1895", film.toString());
            throw new ValidationException("Указана дата релиза раньше, чем 28.12.1895");
        }
    }

}
