package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
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
    public void shouldThrowValidationExceptionWhenEmailEmpty() {
        User user = getUser();
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.create(user), "Не указан почтовый ящик");
    }

    @Test
    public void shouldThrowValidationExceptionWhenEmailNotContainNeededSymbols() {
        User user = getUser();
        user.setEmail("mailmail.ru");
        assertThrows(ValidationException.class, () -> userController.create(user), "Почтовый ящик не содержит символ @");
    }

    @Test
    public void shouldThrowValidationExceptionWhenLoginEmpty() {
        User user = getUser();
        user.setLogin("");
        assertThrows(ValidationException.class, () -> userController.create(user), "Не указан логин");
    }

    @Test
    public void shouldThrowValidationExceptionWhenLoginContainsSpace() {
        User user = getUser();
        user.setLogin("dolore ullamco");
        assertThrows(ValidationException.class, () -> userController.create(user), "Логин содержит пробелы");
    }

    @Test
    public void shouldThrowValidationExceptionWhenNameEmpty() {
        User user = getUser();
        user.setName("");
        userController.create(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void shouldThrowValidationExceptionWhenBirthdayInFuture() {
        User user = getUser();
        user.setBirthday(LocalDate.of(2023, 1, 1));
        assertThrows(ValidationException.class, () -> userController.create(user), "Дата рождения не может быть в будущем");
    }

    @Test
    public void shouldThrowValidationExceptionWhenUnknownUser() {
        User user = getUser();
        userController.create(user);
        User updatedUser = getUser();
        updatedUser.setId(0);
        assertThrows(ValidationException.class, () -> userController.update(updatedUser), "В списке пользователь не найден");
    }


}