package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.exceptions.ClientAlreadyExistsException;
import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Role;
import coms.w4156.moviewishlist.repositories.ClientRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClientService
    extends ServiceForRepository<Long, Client, ClientRepository>
    implements UserDetailsService {

    @Autowired
    RoleService roleService;

    /**
     * Contstructor to set up dependency injection for the ClientRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public ClientService(final ClientRepository repository) {
        this.setRepository(repository);
    }

    private Set<SimpleGrantedAuthority> getAuthority(Client client) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        client.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    /**
     * This should only be used for authentication,
     * as it returns a spring.security.UserDetails object.
     *
     * @param email - The email of the client to find
     * @return The client with the given email
     */
    @Override
    public UserDetails loadUserByUsername(final String email)
        throws UsernameNotFoundException {
        Optional<Client> client = this.getRepository().findByEmail(email);

        if (client.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                client.get().getEmail(),
                "",
                getAuthority(client.get())
            );
        } else {
            throw new UsernameNotFoundException(
                "Client with email " + email + " not found."
            );
        }
    }

    /**
     * Create a new client and return the details for it.
     *
     * @param email - The email of the new client
     * @return The details for the new client
     */
    public UserDetails createClientAndReturnDetails(final String email)
        throws ClientAlreadyExistsException {
        Optional<Client> alreadyExistingClient =
            this.getRepository().findByEmail(email);

        if (alreadyExistingClient.isPresent()) {
            throw new ClientAlreadyExistsException(
                "A client with email " + email + " already exists"
            );
        }

        Set<Role> roles = new HashSet<Role>();
        Role basicClientRole = roleService.findByName("client");
        roles.add(basicClientRole);

        Client client = new Client(email, roles);
        this.getRepository().save(client);

        UserDetails userDetails = this.loadUserByUsername(client.getEmail());

        return userDetails;
    }

     /**
     * Create an admin and return the details for it.
     *
     * @param email - The email of the new admin
     * @return The details for the new admin
     */
    public UserDetails createAdminAndReturnDetails(final String email) {
        Set<Role> roles = new HashSet<Role>();
        Role basicClientRole = roleService.findByName("admin");
        roles.add(basicClientRole);

        Client client = new Client(email, roles);
        this.getRepository().save(client);

        UserDetails userDetails = this.loadUserByUsername(client.getEmail());

        return userDetails;
    }

    /**
     * Get the client with the given email.
     *
     * @param email - The email of the client to find
     * @return The client with the given email
     */
    public Optional<Client> findByEmail(final String email) {
        return this.getRepository().findByEmail(email);
    }
}
