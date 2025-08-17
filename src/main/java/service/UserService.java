package repository;

import hibernate.starter.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;

public class UserService {

    public void createUser(String name, String email, Integer age) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            User user = new User(name, email, age, LocalDateTime.now());
            session.save(user);

            tx.commit();
            System.out.println("Создан пользователь ID=" + user.getId());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public User getUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    public void updateUser(Long id, String newName, String newEmail, Integer newAge) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            User user = session.get(User.class, id);
            if (user != null) {
                user.setName(newName);
                user.setEmail(newEmail);
                user.setAge(newAge);
                session.update(user);
                tx.commit();
                System.out.println("Пользователь обновлен");
            } else {
                System.out.println("Пользователь не найден");
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void deleteUser(Long id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                tx.commit();
                System.out.println("Пользователь удален");
            } else {
                System.out.println("Пользователь не найден");
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}