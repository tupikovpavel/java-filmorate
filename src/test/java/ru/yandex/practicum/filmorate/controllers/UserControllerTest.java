package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void beforeEach() {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        UserService userService = new UserService(inMemoryUserStorage);
        userController = new UserController(userService);
    }

    private User getUser() {
        return User.builder()
                .login("Test")
                .name("name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2022, 1, 1))
                .build();

    }

    @Test
    public void getAll() {
        User user = getUser();
        userController.create(user);
        List<User> users = userController.findAll();
        assertEquals(1, users.size());
    }

    @Test
    public void shouldThrowValidationExceptionWhenLoginContainsSpace() {
        User user = getUser();
        user.setLogin("dolore ullamco");
        assertThrows(ValidationException.class, () -> userController.create(user), "Логин содержит пробелы");
    }

    @Test
    public void shouldThrowValidationExceptionWhenUnknownUser() {
        User user = getUser();
        userController.create(user);
        User updatedUser = getUser();
        updatedUser.setId(0);
        assertThrows(UserNotFoundException.class, () -> userController.update(updatedUser), "В списке пользователь не найден");
    }


}