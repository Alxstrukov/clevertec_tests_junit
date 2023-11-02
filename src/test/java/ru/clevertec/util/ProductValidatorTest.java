package ru.clevertec.util;

import org.junit.jupiter.api.Test;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.util.ProductTestData;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidatorTest {
    private final UUID PRODUCT_UUID = UUID.fromString("7b2119fd-3d14-4a0b-9761-002c48d77368");

    @Test
    void isValidateProductShouldReturnFalse() {
        // given
        ProductValidator validator = new ProductValidator();

        Product product = ProductTestData.builder()
                .withName(null)
                .build().createProduct();

        // when
        boolean actual = validator.isValidateProduct(product);

        // then
        assertFalse(actual);
    }

    @Test
    void isValidateProductShouldReturnTrue() {
        // given
        ProductValidator validator = new ProductValidator();

        Product product = ProductTestData.builder()
                .build().createProduct();

        // when
        boolean actual = validator.isValidateProduct(product);

        // then
        assertTrue(actual);
    }
}