package coms.w4156.moviewishlist.serviceTests;

import coms.w4156.moviewishlist.exceptions.ClientAlreadyExistsException;
import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.repositories.ClientRepository;
import coms.w4156.moviewishlist.services.ClientService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientServiceTests {

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
    void testCreateClientAndReturnDetailsException() {

        String email = "test@test.com";
        Client client = new Client();
        client.setEmail(email);

        Mockito
                .when(clientRepository.findByEmail(email))
                .thenReturn(Optional.ofNullable(client));

        Assertions.assertThrows(ClientAlreadyExistsException.class, () -> clientService.createClientAndReturnDetails(email));
    }

    @Test
    void testFindByEmail() {
        String email = "test@test.com";
        clientService.findByEmail(email);
        verify(clientRepository).findByEmail(email);
    }
}
