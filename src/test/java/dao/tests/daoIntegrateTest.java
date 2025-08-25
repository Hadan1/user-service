package dao.tests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import repository.User;
import repository.UserDao;
import service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(Lifecycle.PER_CLASS)
public class daoTest {
    private static final String TEST_NAME = "Test User";
    private static final String ALT_NAME = "Alt User";
    private static final String TEST_EMAIL = "user@test.com";
    private static final String ALT_EMAIL = "alt@test.com";
    private static final int TEST_AGE = 30;
    private static final int ALT_AGE = 20;

    private SessionFactory sessionFactory;
    private UserDao userDao;
}
