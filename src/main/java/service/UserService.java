package service;

import exceptions.UserException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.User;

import java.time.LocalDateTime;


public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void createUser(String name, String email, Integer age) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = new User(name, email, age, LocalDateTime.now());
            session.save(user);

            transaction.commit();
            System.out.println("Создан пользователь ID=" + user.getId());
            logger.info("Успешное создание");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка создания");
            e.printStackTrace();
        }
    }

    public User getUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            logger.info("Успешное получение");
            return session.get(User.class, id);
        }
        catch (Exception e) {
            logger.error("Ошибка получения");
            throw new UserException(e.getMessage());
        }
    }

    public void updateUser(Long id, String newName, String newEmail, Integer newAge) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            if (user != null) {
                user.setName(newName);
                user.setEmail(newEmail);
                user.setAge(newAge);
                session.update(user);
                transaction.commit();
                System.out.println("Пользователь обновлен");
                logger.info("Успешное обновление");
            } else {
                System.out.println("Пользователь не найден");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Это ошибка");
            throw new UserException(e.getMessage());
        }
    }

    public void deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                transaction.commit();
                System.out.println("Пользователь удален");
                logger.info("Успешное удаление");
            } else {
                System.out.println("Пользователь не найден");
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка удаления");
            throw new UserException(e.getMessage());
        }
    }
}