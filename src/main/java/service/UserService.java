package service;

import exceptions.InvalidUserException;
import exceptions.UserNotFoundException;
import entity.User;
import repository.DAO;
import repository.UserDAO;

import java.util.Optional;

public class UserService {
    private final DAO<User, Long> userDao;

    public UserService() {
        this.userDao = new UserDAO();
    }

    public UserService(DAO<User, Long> userDao) {
        this.userDao = userDao;
    }

    public void saveUser(User user) {
        validateUser(user);
        userDao.save(user);
    }

    public Optional<User> getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidUserException("Некорректный ID пользователя");
        }
        return userDao.getById(id);
    }

    public void updateUser(User user) {
        if (userNotExist(user.getId())) {
            throw new UserNotFoundException("Пользователь не существует");
        }
        userDao.update(user);
    }

    public void deleteUser(User user) {
        if (userNotExist(user.getId())) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        userDao.delete(user.getId());
    }

    private boolean userNotExist(Long id) {
        return id == null || userDao.getById(id).isEmpty();
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new InvalidUserException("Пользователь не может быть null");
        }
    }
}