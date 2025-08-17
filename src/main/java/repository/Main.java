package repository;

import service.UserConsole;
import service.UserService;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService();
    private static final UserConsole userConsole = new UserConsole(scanner, userService);

    public static void main(String[] args) {
        userConsole.run();
    }
}