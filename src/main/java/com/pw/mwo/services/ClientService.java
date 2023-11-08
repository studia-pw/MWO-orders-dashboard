package com.pw.mwo.services;

import com.pw.mwo.domain.Client;
import com.pw.mwo.exceptions.EntityAlreadyExistsException;
import com.pw.mwo.exceptions.EntityNotFoundException;
import com.pw.mwo.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional
    public Client createClient(Client client) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new EntityAlreadyExistsException();
        }

        client.setId(null);
        return clientRepository.save(client);
    }

    @Transactional
    public void deleteClient(long id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }

        clientRepository.deleteById(id);
    }

    @Transactional
    public Client updateClient(Client client) {
        if (!clientRepository.existsById(client.getId())) {
            throw new EntityNotFoundException();
        }

        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public Client getClient(long id) {
        return clientRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Client> getAll() {
        return clientRepository.findAll();
    }
}
