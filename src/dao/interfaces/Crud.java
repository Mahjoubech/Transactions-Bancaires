package dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Crud <T> {
    void create(T Obj);
    void update(T Obj);
    void delete(String id);
    Optional<T> findById(String id);
    List<T> findAll();
}
