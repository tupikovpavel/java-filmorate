package ru.yandex.practicum.filmorate.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    protected int idCounter = 0;

    @Override
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        validate(user);
        idCounter++;
        user.setId(idCounter);
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.debug("Добавленный пользователь {}",user.toString());
        return user;
    }

    @Override
    public User update(User user) {
        validate(user);
        if (!users.containsKey(user.getId())) {
            log.warn("Список пользователей не содержит {}", user.toString());
            throw new UserNotFoundException("В списке пользователь не найден");
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(User user) {
        validate(user);
        if (!users.containsKey(user.getId())) {
            log.warn("Список пользователей не содержит {}", user.toString());
            throw new ValidationException("В списке пользователь не найден");
        }
        users.remove(user);
    }

    private void validate(User user) {

        String login = user.getLogin();
        if (login.contains(" ")) {
            log.warn("Логин у пользователя {} содержит пробелы", user.toString());
            throw new ValidationException("Логин содержит пробелы");
        }
        if ((user.getName() == null) || (user.getName().isBlank())){
            user.setName(login);
        }

    }
}
