package ru.clevertec.product.util;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class ProductTestData {

    @Builder.Default
    private UUID uuid = UUID.fromString("7b2119fd-3d14-4a0b-9761-002c48d77368");

    @Builder.Default
    private String name = "Лимон";

    @Builder.Default
    private String description = "Желтый и сочный";

    @Builder.Default
    private BigDecimal price = BigDecimal.valueOf(2.0);

    @Builder.Default
    private LocalDateTime created = LocalDateTime.of(2023, Month.OCTOBER, 31, 12, 15);

    public Product createProduct() {
        return new Product(uuid, name, description, price, created);
    }

    public ProductDto createProductDto() { return new ProductDto(name, description, price);}

    public InfoProductDto createInfoProductDto() { return new InfoProductDto(uuid, name, description, price); }

}
