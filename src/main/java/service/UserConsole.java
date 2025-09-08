package service;

import entity.User;
import repository.UserDAO;

import java.util.Optional;
import java.util.Scanner;

public class UserConsole {
    private final Scanner scanner;
    private final UserDAO userDAO;
    private boolean running = true;

    public UserConsole(Scanner scanner, UserDAO userDAO) {
        this.scanner = scanner;
        this.userDAO = userDAO;
    }

    public void run() {
        while (running) {
            try {
                printMenu();
                int choice = getIntInput();

                switch (choice) {
                    case 1 -> createUser();
                    case 2 -> findUserById();
                    case 3 -> updateUser();
                    case 4 -> deleteUser();
                    case 5 -> running = false;
                    default -> System.out.println("Неверный выбор, попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("1. Создать пользователя");
        System.out.println("2. Найти пользователя по ID");
        System.out.println("3. Обновить пользователя");
        System.out.println("4. Удалить пользователя");
        System.out.println("5. Выход");
        System.out.print("Выберите действие: ");
    }

    private int getIntInput() {
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }

    private long getLongInput() {
        long input = scanner.nextLong();
        scanner.nextLine();
        return input;
    }

    private void createUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        System.out.print("Введите возраст: ");
        int age = getIntInput();

        User user = new User(name, email, age);
        userDAO.save(user);
        // сообщение о успешном создании выводится в консоль в блоке try-catch в UserService
    }

    private void findUserById() {
        System.out.print("Введите ID пользователя: ");
        Long id = getLongInput();

        Optional<User> user = userDAO.getById(id);
        user.ifPresentOrElse(
                u -> System.out.println("Найден пользователь: " + u),
                () -> System.out.println("Пользователь с ID " + id + " не найден")
        );
    }

    private void updateUser() {
        System.out.print("Введите ID пользователя для обновления: ");
        Long id = getLongInput();

        Optional<User> optionalUser = userDAO.getById(id);
        if (optionalUser.isEmpty()) {
            System.out.println("Пользователь не найден");
            return;
        }

        User user = optionalUser.get();

        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        System.out.print("Введите возраст: ");
        int age = getIntInput();
        User newUser =  new User(name, email, age);
        userDAO.update(newUser);
    }

    private void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = getLongInput();
        userDAO.delete(id);
    }
}
