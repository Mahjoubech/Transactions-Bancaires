package service;

import dao.ClientRepository;
import dao.CompteRepository;
import entity.Client;
import entity.Compte;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RapportService implements RapportServiceInterface {
    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;
    public RapportService(CompteRepository compteRepository, ClientRepository clientRepository) {
        this.compteRepository = compteRepository;
        this.clientRepository = clientRepository;
    }

    public double totalSolde(String idClient){
        List<Compte> cmp = compteRepository.findAll().
                stream().
                filter(c -> c.getIdClient().equals(idClient)).
                toList();
        return cmp.stream()
                .mapToDouble(Compte::getSolde)
                .sum();
    }
    @Override
    public List<Client>  topFiveClientsSolde(){
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .sorted((a , b)->Double.compare(totalSolde(b.id()) ,totalSolde(a.id())))
                .limit(5)
                .collect(Collectors.toList());
    }

}
