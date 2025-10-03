package dao;
import entity.Client;
import java.util.List;
import java.util.Optional;

public interface ClientRepository extends Crud <Client> {
    List<Client> findByName(String name);
    Optional<Client> findByEmail(String email);
}
