package com.pw.mwo.repositories;

import com.pw.mwo.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    @Override
    List<Product> findAll();

    @Override
    List<Product> findAllById(Iterable<Long> ids);
    boolean existsByName(String name);
}
