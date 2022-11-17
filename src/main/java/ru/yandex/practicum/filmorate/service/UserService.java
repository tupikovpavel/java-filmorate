package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
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

    public User addUserFriend(Integer userId, Integer friendId) {

        if (userId == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    userId));
        }
        if (friendId == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    friendId));
        }
        User user = findUserById(userId);
        validate(user, userId);
        User friend = findUserById(friendId);
        validate(friend, friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return user;

    }

    public User removeUserFriend(Integer userId, Integer friendId) {
        if (userId == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    userId));
        }
        if (friendId == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    friendId));
        }
        User user = findUserById(userId);
        validate(user, userId);
        User friend = findUserById(friendId);
        validate(friend, friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return user;
    }

    public List<User> findAllFriends(Integer userId) {
        List<User> friends = new ArrayList<>();
        for (Integer friendId : findUserById(userId).getFriends()) {
            friends.add(findUserById(friendId));
        }
        return friends;
    }

    public List<User> findCommonFriends(Integer userId, Integer otherUserId) {
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> userFriends = findUserById(userId).getFriends();
        Set<Integer> otherUserFriends = findUserById(otherUserId).getFriends();
        if ((userFriends == null) || (userFriends.size() == 0)) {
            return commonFriends;
        }
        if ((otherUserFriends == null) || (otherUserFriends.size() == 0)) {
            return commonFriends;
        }
        for (Integer friendId : userFriends) {
            if (otherUserFriends.contains(friendId)) {
                commonFriends.add(findUserById(friendId));
            }
        }
        return commonFriends;
    }

    public User findUserById(Integer userId) {
        return userStorage.findAll().stream().filter(p -> p.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException(String.format("Пользователь № %d не найден", userId)));
    }

    private void validate(User user, Integer userId) {

        if (user == null) {
            throw new UserNotFoundException(String.format(
                    "Пользователь %s не найден",
                    userId));
        }

    }

}
