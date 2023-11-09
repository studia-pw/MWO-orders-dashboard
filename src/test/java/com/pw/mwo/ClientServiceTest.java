package com.pw.mwo;

import com.pw.mwo.domain.Client;
import com.pw.mwo.repositories.ClientRepository;
import com.pw.mwo.services.ClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    private ClientService clientService;

    private static final Logger logger = LogManager.getLogger();

    @BeforeEach
    void beforeEach() {
        clientService = new ClientService(clientRepository);
    }

    @ParameterizedTest
    @MethodSource("methodSource")
    void Should_ReturnListOfClients_When_GetAllClientsInvoked(List<Client> clientList) {
        // arrange
        logger.info("Mocking clientRepository");
        List<Client> expected = clientList;
        when(clientRepository.findAll()).thenReturn(expected);

        // act
        logger.warn("Invoking getAll() on clientService");
        List<Client> clients = clientService.getAll();

        // assert
        assertThat(clients).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    void Should_GetClientById_WhenGetClientInvoked(long id) {
        // arrange
        Client expected = ClientFactory.makeClientCase1();
        expected.setId(id);
        when(clientRepository.findById(id)).thenReturn(Optional.of(expected));

        // act
        Client client = clientService.getClient(id);

        // assert
        assertThat(client)
                .extracting("id", "name", "surname", "email")
                .contains(id, expected.getName(), expected.getSurname(), expected.getEmail());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/data.csv")
    void Should_DeleteClient_When_DeleteClientInvokedAndClientExists(long id, boolean expected) {
        // arrange
        when(clientRepository.existsById(id)).thenReturn(expected);

        // act
        clientService.deleteClient(id);

        // assert
        verify(clientRepository, times(1)).deleteById(id);

    }

    private static Stream<Arguments> methodSource() {
        return Stream.of(
                Arguments.of(List.of(ClientFactory.makeClientCase1())),
                Arguments.of(List.of(ClientFactory.makeClientCase1(), ClientFactory.makeClientCase2())),
                Arguments.of(List.of(ClientFactory.makeClientCase1(), ClientFactory.makeClientCase2(), ClientFactory.makeClientCase3()))
        );
    }
}
