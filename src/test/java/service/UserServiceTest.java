package service;

import exceptions.InvalidUserException;
import exceptions.UserNotFoundException;
import entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.DAO;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private User createNewUser() {
        return new User("Name", "valid@email.com", 30);
    }

    @Mock
    private DAO<User, Long> DAO;

    @InjectMocks
    private UserService userService;

    @Test
    void testSaveUserSuccessfully() {
        User user = createNewUser();
        userService.saveUser(user);
        verify(DAO).save(user);
    }

    @Test
    void testSaveUserNullThrowsInvalidUserException() {
        assertThrows(InvalidUserException.class, () -> userService.saveUser(null));
        verifyNoInteractions(DAO);
    }

    @Test
    void testGetUserByInvalidIdThrowsInvalidUserException() {
        assertThrows(InvalidUserException.class, () -> userService.getUserById(0L));
        verifyNoInteractions(DAO);
    }

    @Test
    void testUpdateUserUpdatesSuccessfully() {
        User user = createNewUser();
        user.setId(1L);
        when(DAO.getById(user.getId())).thenReturn(Optional.of(user));

        userService.updateUser(user);
        verify(DAO).update(user);
    }

    @Test
    void testUpdateUserNonExistThrowsUserNotFoundException() {
        User user = createNewUser();
        user.setId(999L);
        when(DAO.getById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));
        verify(DAO, never()).update(any());
    }

    @Test
    void testDeleteUserDeletesSuccessfully() {
        User user = createNewUser();
        user.setId(1L);
        when(DAO.getById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUser(user);
        verify(DAO).delete(user.getId());
    }

    @Test
    void testDeleteNonExistentUserThrowsUserNotFoundException() {
        User user = createNewUser();
        user.setId(999L);
        when(DAO.getById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(user));
        verify(DAO, never()).delete(any());
    }

}