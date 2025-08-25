package repository;

public interface UserDao {
        void createUser(User user);
        User getUserById(Long id);
        void updateUser(User user);
        void deleteUser(Long id);
}
