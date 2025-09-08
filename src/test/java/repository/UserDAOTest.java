package repository;

import entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.HibernateUtil;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDAOTest {
    // Константы для контейнера
    private static final String POSTGRES_IMAGE = "postgres:16";
    private static final String DATABASE_NAME = "testdb";
    private static final String DATABASE_USERNAME = "testuser";
    private static final String DATABASE_PASSWORD = "testpass";

    // Константы для тестовых данных
    private static final String TEST_USER_NAME = "John Johnson";
    private static final String TEST_USER_EMAIL = "john@example.com";
    private static final int TEST_USER_AGE = 25;
    private static final String UPDATED_NAME = "Updated Name";
    private static final long NON_EXISTENT_ID = 999L;

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>(POSTGRES_IMAGE)
                    .withDatabaseName(DATABASE_NAME)
                    .withUsername(DATABASE_USERNAME)
                    .withPassword(DATABASE_PASSWORD);

    private static SessionFactory sessionFactory;
    private UserDAO userDAO;

    @BeforeAll
    static void setup() {
        assertTrue(POSTGRES_CONTAINER.isRunning(), "PostgreSQL container should be running");
        System.setProperty("hibernate.connection.url", POSTGRES_CONTAINER.getJdbcUrl());
        System.setProperty("hibernate.connection.username", POSTGRES_CONTAINER.getUsername());
        System.setProperty("hibernate.connection.password", POSTGRES_CONTAINER.getPassword());

        sessionFactory = HibernateUtil.getSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            assertTrue(session.isConnected(), "Should be connected to database");
        }
    }

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
        clearDatabase();
    }

    @AfterAll
    static void cleanup() {
        HibernateUtil.shutdown();
    }

    private void clearDatabase() {
        sessionFactory.inTransaction(session -> {
            session.createNativeQuery(
                    "TRUNCATE TABLE users RESTART IDENTITY"
            ).executeUpdate();
        });
    }

    private User persistTestUser(String name, String email, int age) {
        return sessionFactory.fromTransaction(session -> {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setAge(age);
            user.setCreatedAt(LocalDateTime.now());
            session.persist(user);
            return user;
        });
    }

    @Test
    void testFindById() {
        User user = persistTestUser(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_AGE);
        Optional<User> found = userDAO.getById(user.getId());
        assertTrue(found.isPresent());
        assertEquals(user.getName(), found.get().getName());
        assertEquals(user.getEmail(), found.get().getEmail());
        assertEquals(user.getAge(), found.get().getAge());
    }

    @Test
    void testUpdateModifyExistingUser() {
        // Подготовка данных
        User originalUser = persistTestUser(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_AGE);

        originalUser.setName(UPDATED_NAME);

        userDAO.update(originalUser);

        User updatedUser = sessionFactory.fromTransaction(session ->
                session.find(User.class, originalUser.getId())
        );

        assertEquals(UPDATED_NAME, updatedUser.getName());
        assertEquals(TEST_USER_EMAIL, updatedUser.getEmail());
        assertEquals(TEST_USER_AGE, updatedUser.getAge());
    }

    @Test
    void save_shouldCreateNewUser() {
        User newUser = new User();
        newUser.setName(TEST_USER_NAME);
        newUser.setEmail(TEST_USER_EMAIL);
        newUser.setAge(TEST_USER_AGE);
        newUser.setCreatedAt(LocalDateTime.now());
        userDAO.save(newUser);
        User savedUser = sessionFactory.fromTransaction(session ->
                session.find(User.class, newUser.getId())
        );
        assertNotNull(savedUser);
        assertEquals(TEST_USER_NAME, savedUser.getName());
        assertEquals(TEST_USER_EMAIL, savedUser.getEmail());
        assertEquals(TEST_USER_AGE, savedUser.getAge());
    }

    @Test
    void testRemoveUser() {
        User userToDelete = persistTestUser(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_AGE);
        userDAO.delete(userToDelete.getId());
        Optional<User> deletedUser = sessionFactory.fromTransaction(session ->
                Optional.ofNullable(session.find(User.class, userToDelete.getId()))
        );
        assertFalse(deletedUser.isPresent());
    }


    @Test
    void testFindByIdReturnEmptyOptional() {
        Optional<User> result = userDAO.getById(NON_EXISTENT_ID);
        assertFalse(result.isPresent());
    }
}
