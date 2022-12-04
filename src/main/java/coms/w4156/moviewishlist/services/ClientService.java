package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.exceptions.ClientAlreadyExistsException;
import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.repositories.ClientRepository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class ClientService
    extends ServiceForRepository<Long, Client, ClientRepository> implements UserDetailsService {

    /**
     * Contstructor to set up dependency injection for the ClientRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public ClientService(final ClientRepository repository) {
        this.setRepository(repository);
    }

    // This should only be used for authentication, as it returns a spring.security.UserDetails
    @Override public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Client> client = this.getRepository().findByEmail(email);

        if (client.isPresent()) {
            return new org.springframework.security.core.userdetails.User(client.get().getEmail(), "", new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Client with email " + email + " not found.");
        }
    }

    public UserDetails createClientAndReturnDetails(String email) throws ClientAlreadyExistsException {
        Optional<Client> alreadyExistingClient = this.getRepository().findByEmail(email);

        if (alreadyExistingClient.isPresent()) {
            throw new ClientAlreadyExistsException("A client with email " + email + " already exists");
        }

        Client client = new Client(email);
        this.getRepository().save(client);

        UserDetails userDetails = this.loadUserByUsername(client.getEmail());

        return userDetails;
    }

    public Optional<Client> findByEmail(String email) {
        return this.getRepository().findByEmail(email);
    }


}
