package repository;

import exceptions.UserException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import entity.User;
import utils.HibernateUtil;

import java.util.Optional;


public class UserDAO implements DAO<User, Long> {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private final SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public UserDAO() {
        this(HibernateUtil.getSessionFactory());
    }

    @Override
    public void save(User user) {
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
    public Optional<User> getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            logger.info("Успешное получение");
            return Optional.ofNullable(session.find(User.class, id));
        }
        catch (Exception e) {
            logger.error("Ошибка получения");
            throw new UserException(e.getMessage());
        }
    }

    @Override
    public void update(User user) {
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
            logger.error("Ошибка обновления");
            throw new UserException(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
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