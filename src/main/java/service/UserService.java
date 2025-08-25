package service;

import exceptions.UserException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.User;
import repository.UserDao;

import java.time.LocalDateTime;


public class UserService implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final SessionFactory sessionFactory;

    public UserService(Session session) {
        this.sessionFactory = sessionFactory;
    }

    public UserService() {
        this(HibernateUtil.getSessionFactory());
    }

    @Override
    public void createUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            System.out.println("Создан пользователь ID=" + user.getId());
            logger.info("Успешное создание");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Ошибка создания");
            e.printStackTrace();
        }
    }

    @Override
    public User getUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            logger.info("Успешное получение");
            return session.find(User.class, id);
        }
        catch (Exception e) {
            logger.error("Ошибка получения");
            throw new UserException(e.getMessage());
        }
    }

    @Override
    public void updateUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            if (user != null) {
                user.setName(user.getName());
                user.setEmail(user.getEmail());
                user.setAge(user.getAge());
                session.merge(user);
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

    @Override
    public void deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = session.find(User.class, id);
            if (user != null) {
                session.remove(user);
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