package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validate(user);
        return userStorage.update(user);
    }

    public void delete(User user) {
        validate(user);
        userStorage.delete(user);
    }

    public User addFriend(Integer userId, Integer friendId) {

        User user = findById(userId);
        checkIfNotNull(user, userId);
        User friend = findById(friendId);
        checkIfNotNull(friend, friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return user;

    }

    public User removeFriend(Integer userId, Integer friendId) {

        User user = findById(userId);
        checkIfNotNull(user, userId);
        User friend = findById(friendId);
        checkIfNotNull(friend, friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return user;

    }

    public List<User> findAllFriends(Integer userId) {
        List<User> friends = new ArrayList<>();
        for (Integer friendId : findById(userId).getFriends()) {
            friends.add(findById(friendId));
        }
        return friends;
    }

    public List<User> findCommonFriends(Integer userId, Integer otherUserId) {
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> userFriends = findById(userId).getFriends();
        Set<Integer> otherUserFriends = findById(otherUserId).getFriends();
        if ((userFriends == null) || (userFriends.size() == 0)) {
            return commonFriends;
        }
        if ((otherUserFriends == null) || (otherUserFriends.size() == 0)) {
            return commonFriends;
        }
        for (Integer friendId : userFriends) {
            if (otherUserFriends.contains(friendId)) {
                commonFriends.add(findById(friendId));
            }
        }
        return commonFriends;
    }

    public User findById(Integer userId) {
        return userStorage.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format(
                "Пользователь %s не найден",
                userId)));
    }

    private void checkIfNotNull(User user, Integer userId) {

        if (user == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    userId));
        }

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
