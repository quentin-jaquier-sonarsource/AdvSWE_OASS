package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService
    extends ServiceForRepository<Long, Client, ClientRepository> {

    /**
     * Contstructor to set up dependency injection for the ClientRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public ClientService(final ClientRepository repository) {
        this.setRepository(repository);
    }
}
