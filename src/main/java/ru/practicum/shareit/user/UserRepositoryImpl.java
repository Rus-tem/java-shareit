package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final List<User> listOfUsers = new ArrayList<>();

    @Override
    public List<User> findAll() {
        return listOfUsers;
    }

    @Override
    public User save(User user) {
        listOfUsers.add(user);
        return user;
    }

    @Override
    public User update(User oldUser, User newUser) {
        User user = new User(oldUser.getId(), newUser.getEmail(), newUser.getName());
        delete(oldUser);
        listOfUsers.add(user);
        return user;
    }

    @Override
    public void delete(User user) {
        listOfUsers.remove(user);
    }
}