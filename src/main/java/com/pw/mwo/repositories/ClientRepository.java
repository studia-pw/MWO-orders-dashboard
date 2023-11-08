package com.pw.mwo.repositories;

import com.pw.mwo.domain.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    @Override
    List<Client> findAll();

    boolean existsByEmail(String email);
}
