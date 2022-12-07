package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;
import java.util.stream.Collectors;

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
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void delete(Integer userId) {
        userStorage.delete(userId);
    }

    public User addFriend(Integer userId, Integer friendId) {
        User user = findById(userId);
        User friend = findById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return user;
    }
    public User removeFriend(Integer userId, Integer friendId) {
        User user = findById(userId);
        User friend = findById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return user;
    }

    public List<User> findAllFriends(Integer userId) {
        return findById(userId).getFriends().stream().map(this::findById).collect(Collectors.toList());
    }

    public List<User> findCommonFriends(Integer userId, Integer otherUserId) {
        return findById(userId).getFriends()
                .stream()
                .filter(friendId -> findById(otherUserId).getFriends().contains(friendId))
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public User findById(Integer userId) {
        return userStorage.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format(
                "Пользователь %s не найден",
                userId)));
    }

}
