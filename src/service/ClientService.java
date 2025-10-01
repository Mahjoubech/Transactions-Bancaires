package service;

import dao.interfaces.ClientRepository;
import dao.repository.ClientRepoImp;
import model.entity.Client;
import service.interfaces.ClientServiceInterface;
import util.Helper;
import util.Validateur;

import java.util.List;
import java.util.Optional;

public class ClientService implements ClientServiceInterface {
    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    @Override
    public  Client create(String nom, String email) {
        try {
            String id = Helper.generateIDClient();
            if (!Validateur.isValidClientId(id))
                throw new IllegalArgumentException("Format code compte invalide ! (CLI-XXXXX)");
            if (!Validateur.isValidEmail(email))
                throw new IllegalArgumentException("Format email invalide !");
            Client client = new Client(id, nom, email);
            clientRepository.create(client);
            return client;
        } catch (Exception e) {
            System.out.println("Error creating client: " + e.getMessage());
            return null;
        }
    }
    @Override
    public void update(String id , String nom , String email) {
        try{
            Client client = new Client(id , nom , email);
            clientRepository.update(client);
        }catch (Exception e){
            System.out.println("Error updating client: " + e.getMessage());
        }
    }
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    @Override
    public void delete(Client c){
        try {
            clientRepository.delete(c.id());
        } catch (Exception e) {
            System.out.println("Error deleting client: " + e.getMessage());
        }
    };
    @Override
    public Optional<Client> findById(String id) {
        return clientRepository.findById(id);

    }
    @Override
    public List<Client> getByNom(String name) {
        return clientRepository.findByName(name);
    }
    @Override
    public Optional<Client> getByEmail(String email) {
        return clientRepository.findByEmail(email);
    }


}
