package service.interfaces;

import model.entity.Client;
import java.util.List;
import java.util.Optional;

public interface ClientServiceInterface {
    void create(String nom , String email);
    void update(String id , String nom , String email);
    void delete(Client c);
    Optional<Client> findById(String id);
    List<Client> getAllClients();
    List<Client> getByNom(String name);
    Optional<Client> getByEmail(String email);

}

