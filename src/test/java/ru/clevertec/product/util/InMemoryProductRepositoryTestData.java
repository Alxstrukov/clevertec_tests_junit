package ru.clevertec.product.util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.mapper.impl.ProductMapperImpl;
import ru.clevertec.product.repository.impl.InMemoryProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class InMemoryProductRepositoryTestData {

    @Builder.Default
    ProductMapper mapper = new ProductMapperImpl();

    @Builder.Default
    private HashMap<UUID, Product> storage =
            new HashMap<>(Map.of(
                    UUID.fromString("24b5c3c5-fa2d-45ca-a7ec-0029c7d0f225"),
                    new Product(UUID.fromString("24b5c3c5-fa2d-45ca-a7ec-0029c7d0f225"),
                            "Яблоко",
                            "Желтое и вкусное",
                            BigDecimal.valueOf(2.77),
                            LocalDateTime.of(2023, 12, 31, 0, 0)
                    ), UUID.fromString("21b5c3c5-fa2d-45ca-a7ec-0029c7d0f225"),
                    new Product(UUID.fromString("21b5c3c5-fa2d-45ca-a7ec-0029c7d0f225"),
                            "Груша",
                            "Зеленая и сочная",
                            BigDecimal.valueOf(2.77),
                            LocalDateTime.of(2023, 12, 31, 0, 0)
                    ), UUID.fromString("27b7c3c5-fa2d-45ca-a7ec-0029c7d0f225"),
                    new Product(UUID.fromString("27b7c3c5-fa2d-45ca-a7ec-0029c7d0f225"),
                            "Вишня",
                            "Сладкая и крупная",
                            BigDecimal.valueOf(2.77),
                            LocalDateTime.of(2023, 12, 31, 0, 0)
                    )));


    public InMemoryProductRepository createInMemoryRepository() {
        InMemoryProductRepository repository = new InMemoryProductRepository();
        storage.forEach((key, value) -> repository.save(value));
        return repository;
    }

    public List<Product> getProducts() {
        return storage.values().stream().toList();
    }

    public List<InfoProductDto> getInfoProducts() {
        return storage.values().stream()
                .map(p -> mapper.toInfoProductDto(p))
                .toList();
    }
}
