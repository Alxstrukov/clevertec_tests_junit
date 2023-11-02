package ru.clevertec.product.repository.impl;

import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductNotFoundException;
import ru.clevertec.product.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryProductRepository implements ProductRepository {

    private final HashMap<UUID, Product> dataStorage = new HashMap<>();

    @Override
    public Optional<Product> findById(UUID uuid) {
        return Optional.ofNullable(dataStorage.get(uuid));
    }

    @Override
    public List<Product> findAll() {
        Collection<Product> values = dataStorage.values();
        return new ArrayList<>(values);
    }

    @Override
    public Product save(Product product) {
        if (product == null) throw new IllegalArgumentException();
        if (dataStorage.containsKey(product.getUuid())) {
            product.setName(product.getName())
                    .setDescription(product.getDescription())
                    .setPrice(product.getPrice())
                    .setCreated(LocalDateTime.now());
        } else {
            product.setUuid(UUID.randomUUID())
                    .setCreated(LocalDateTime.now());
        }
        dataStorage.put(product.getUuid(), product);
        return dataStorage.get(product.getUuid());
    }

    @Override
    public void delete(UUID uuid) {
        if (!dataStorage.containsKey(uuid)) throw new ProductNotFoundException(uuid);
        dataStorage.remove(uuid);
    }
}
