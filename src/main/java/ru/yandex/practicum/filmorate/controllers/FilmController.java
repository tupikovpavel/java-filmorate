package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }


    @GetMapping("/{id}")
    public Film findById(@PathVariable("id") Integer id) {
        return filmService.findById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film update(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    @Validated
    public List<Film> findAll(@RequestParam(value = "count", defaultValue = "10", required = false) @Positive Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        return filmService.findAll(count);
    }

}
