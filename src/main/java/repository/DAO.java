package repository;

import java.util.Optional;

public interface DAO<T, ID> {
        void save(T entity);
        Optional<T> getById(ID id);
        void update(T entity);
        void delete(ID id);
}
