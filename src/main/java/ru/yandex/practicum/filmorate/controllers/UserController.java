package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    //private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();

    protected int idCounter = 0;

    @GetMapping()
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        validate(user);
        idCounter++;
        user.setId(idCounter);
        users.put(user.getId(), user);
        log.debug("Добавленный пользователь {}",user.toString());
        return user;
    }

    @PutMapping()
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Список пользователей не содержит {}", user.toString());
            throw new ValidationException("В списке пользователь не найден");
        }
        validate(user);
        users.put(user.getId(), user);
        return user;
    }

    private void validate(User user) {

        String email = user.getEmail();
        String login = user.getLogin();
        if ((email == null) || (email.isBlank())) {
            log.warn("Не указан почтовый ящик у пользователя {}", user.toString());
            throw new ValidationException("Не указан почтовый ящик");
        }
        if (!email.contains("@")) {
            log.warn("Почтовый ящик у пользователя {} не содержит символ @", user.toString());
            throw new ValidationException("Почтовый ящик не содержит символ @");
        }
        if ((login == null) || (login.isBlank())) {
            log.warn("Не указан логин у пользователя {}", user.toString());
            throw new ValidationException("Не указан логин");
        }
        if (login.contains(" ")) {
            log.warn("Логин у пользователя {} содержит пробелы", user.toString());
            throw new ValidationException("Логин содержит пробелы");
        }
        if ((user.getName() == null) || (user.getName().isBlank())){
            user.setName(login);
        }
        if ((user.getBirthday() != null) && (user.getBirthday().isAfter(LocalDate.now()))) {
            log.warn("Указана будущая дата рождения у пользователя {}", user.toString());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

    }

}
