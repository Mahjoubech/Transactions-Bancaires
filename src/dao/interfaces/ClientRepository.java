package dao.interfaces;
import model.entity.Client;
import java.util.List;
public interface ClientRepository extends Crud <Client> {
    List <Client> findByName(String name);
    List<Client> findByEmail(String email);
}
