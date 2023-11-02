package ru.clevertec.product.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.InvalidProductException;
import ru.clevertec.product.mapper.ProductMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductMapperImplTest {

    static final UUID PRODUCT_UUID = UUID.fromString("27b5c3c5-fa2d-45ca-a7ec-0029c7d0f225");

    Product product;

    ProductMapper productMapper = new ProductMapperImpl();

    @BeforeEach
    void setUp() {
        product = new Product(PRODUCT_UUID,
                "Яблоко",
                "Красное большое",
                BigDecimal.valueOf(1.70),
                LocalDateTime.of(2023, 10, 29, 18, 0));
    }

    public static Stream<ProductDto> generateArgProductDto() {
        ArrayList<ProductDto> p = new ArrayList<>();
        p.add(new ProductDto("Яблоко", "Зеленое кислое", BigDecimal.valueOf(1.57)));
        p.add(new ProductDto("Вишня", "Вкусная сладкая", BigDecimal.valueOf(4.57)));
        p.add(new ProductDto("Лимон", "Желтый сочный", BigDecimal.valueOf(2.7)));

        return p.stream();
    }


    @ParameterizedTest
    @MethodSource("generateArgProductDto")
    void toProductShouldReturnProduct(ProductDto productDto) {
        // given
        Product expected = new Product(
                null,
                productDto.name(),
                productDto.description(),
                productDto.price(),
                null);

        // when
        Product actual = productMapper.toProduct(productDto);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void toProductShouldReturnInvalidProductException() {
        // given
        ProductDto productDto = new ProductDto("$%#&_", null, null);

        // when, then
        assertThrows(InvalidProductException.class, () -> productMapper.toProduct(productDto));
    }

    @Test
    void toInfoProductDtoShouldReturnInfoProduct() {
        // given
        InfoProductDto expected = new InfoProductDto(
                product.getUuid(),
                product.getName(),
                product.getDescription(),
                product.getPrice());

        //when
        InfoProductDto actual = productMapper.toInfoProductDto(product);

        //then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("generateArgProductDto")
    void mergeShouldReturnProduct(ProductDto productDto) {
        // given
        Product expected = new Product(product.getUuid(),
                productDto.name(),
                productDto.description(),
                productDto.price(),
                product.getCreated());

        // when
        Product actual = productMapper.merge(product, productDto);

        // then
        assertEquals(expected, actual);
    }
}