package dao.tests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.User;
import service.UserService;

import java.time.LocalDateTime;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class daoMockitoTest {
    @Test
    void saveUser_savesSuccessfully() {
        Session mockSession = mock(Session.class);
        Transaction mockTransaction = mock(Transaction.class);
        when(mockSession.beginTransaction()).thenReturn(mockTransaction);
        UserService userService = new UserService(mockSession);

    }

    private User createNewUser() {
        return new User("Name", "valid@email.com", 30, LocalDateTime.now());
    }

}

