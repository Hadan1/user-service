import service.UserConsole;
import repository.UserDAO;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDAO USER_DAO = new UserDAO();
    private static final UserConsole userConsole = new UserConsole(scanner, USER_DAO);

    public static void main(String[] args) {
        userConsole.run();
    }
}