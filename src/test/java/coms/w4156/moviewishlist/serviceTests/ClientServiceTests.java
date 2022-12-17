package coms.w4156.moviewishlist.serviceTests;

import coms.w4156.moviewishlist.exceptions.ClientAlreadyExistsException;
import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.repositories.ClientRepository;
import coms.w4156.moviewishlist.repositories.RatingRepository;
import coms.w4156.moviewishlist.services.ClientService;
import coms.w4156.moviewishlist.services.RatingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    /**
     * Tests that exception is thrown if client email not present.
     */
    @Test()
    void testLoadUserByUserNameException() {

        String email = "test@test.com";

        Mockito
                .when(clientRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> clientService.loadUserByUsername(email));
    }

    @Test()
    void testLoadUserByUserName() {

        String email = "test@test.com";
        Client client = new Client();
        client.setEmail(email);

        Mockito
                .when(clientRepository.findByEmail(email))
                .thenReturn(Optional.ofNullable(client));

        org.springframework.security.core.userdetails.User expectedUser = new User(client.getEmail(), "", new ArrayList<>());
        UserDetails returnedUser = clientService.loadUserByUsername(email);

        Assertions.assertEquals(expectedUser, returnedUser);
    }

    @Test()
    void testCreateClientAndReturnDetailsException() {

        String email = "test@test.com";
        Client client = new Client();
        client.setEmail(email);

        Mockito
                .when(clientRepository.findByEmail(email))
                .thenReturn(Optional.ofNullable(client));

        Assertions.assertThrows(ClientAlreadyExistsException.class, () -> clientService.createClientAndReturnDetails(email));
    }

    @Test()
    void testCreateClientAndReturnDetails() throws ClientAlreadyExistsException {

        String email = "test@test.com";
        Client client = new Client();
        client.setEmail(email);

        UserDetails expected = new User(client.getEmail(), "", new ArrayList<>());

        Mockito
                .when(clientRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        Mockito
                .when(clientRepository.save(client))
                .thenReturn(client);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> clientService.createClientAndReturnDetails(email));

        verify(clientRepository).save(client);

    }

    @Test
    void testFindByEmail() {
        String email = "test@test.com";
        clientService.findByEmail(email);
        verify(clientRepository).findByEmail(email);
    }
}
