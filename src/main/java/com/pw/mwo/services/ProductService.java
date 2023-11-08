package com.pw.mwo.services;

import com.pw.mwo.domain.Product;
import com.pw.mwo.exceptions.EntityAlreadyExistsException;
import com.pw.mwo.exceptions.EntityNotFoundException;
import com.pw.mwo.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new EntityAlreadyExistsException();
        }

        product.setId(null);
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }

        productRepository.deleteById(id);
    }

    @Transactional
    public Product updateProduct(Product product) {
        if (!productRepository.existsById(product.getId())) {
            throw new EntityNotFoundException();
        }

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product getProduct(long id) {
        return productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return productRepository.findAll();
    }
}
