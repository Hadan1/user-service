package aston;

import aston.repository.HibernateUtil;
import aston.repository.User;
import aston.repository.UserService;

public class Main {

    public static void main(String[] args) {

        UserService service = new UserService();
        // Создаем пользователя
        service.createUser("Иван Иванов", "ivan@example.com", 30);

        // Обновляем пользователя по ID
        Long userIdToUpdate=1L; // замените на актуальный ID
        service.updateUser(userIdToUpdate, "Иван Иванович", "ivanov@example.com", 31);

        // Удаляем пользователя по ID
        Long userIdToDelete=1L; // замените на актуальный ID
        service.deleteUser(userIdToDelete);

        // Получение пользователя по ID
        User user = service.getUserById(1L);
        if (user != null) {
            System.out.println("Найден пользователь: " + user.getName());
        } else {
            System.out.println("Пользователь не найден");
        }
        HibernateUtil.getSessionFactory().close();

    }
}