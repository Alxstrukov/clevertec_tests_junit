package ru.clevertec.product.repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.builders.InMemoryProductRepositoryTestData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryProductRepositoryTest {

    static final UUID PRODUCT_UUID = UUID.fromString("27b5c3c5-fa2d-45ca-a7ec-0029c7d0f225");

    public static Stream<Product> generateArgsProduct() {
        List<Product> args = List.of(
                new Product(PRODUCT_UUID,
                        "Лимон",
                        "Желтый",
                        BigDecimal.valueOf(2.77),
                        LocalDateTime.of(2023, 12, 31, 0, 0)
                ));

        return args.stream();
    }

    @ParameterizedTest
    @MethodSource("generateArgsProduct")
    void findByIdShouldReturnOptionalEmpty(Product expected) {
        // given
        HashMap<UUID, Product> map = new HashMap<>();
        map.put(PRODUCT_UUID, expected);
        InMemoryProductRepository testRepository = InMemoryProductRepositoryTestData.builder()
                .withStorage(map)
                .build()
                .createInMemoryRepository();

        // when
        Product actual = testRepository.findById(expected.getUuid()).orElseThrow();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void findAllShouldReturnValuesQuantityFromRepository() {
        // given
        InMemoryProductRepository repository = InMemoryProductRepositoryTestData.builder()
                .build().createInMemoryRepository();
        int expected = InMemoryProductRepositoryTestData.builder()
                .build().getProducts().size();

        // when
        int actual = repository.findAll().size();

        // then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @NullSource
    void saveShouldReturnIllegalArgumentException(Product product) {
        InMemoryProductRepository testRepository = InMemoryProductRepositoryTestData.builder()
                .build()
                .createInMemoryRepository();

        assertThrows(IllegalArgumentException.class, () -> {
            testRepository.save(product);
        });
    }

    @Test
    void saveShouldReturnProduct() {
        // given
        InMemoryProductRepository testRepository = InMemoryProductRepositoryTestData.builder()
                .build()
                .createInMemoryRepository();

        UUID uuid = testRepository.findAll().get(0).getUuid();

        Product expected = Product.builder()
                .uuid(uuid)
                .build();

        // when
        Product actual = testRepository.save(expected);

        // then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(value = "24b5c3c5-fa2d-45ca-a7ec-0029c7d0f225")
    void deleteShouldDeleteByKey() {
        // given
        InMemoryProductRepository testRepository = InMemoryProductRepositoryTestData.builder()
                .build()
                .createInMemoryRepository();

        UUID uuid = testRepository.findAll().get(0).getUuid();

        testRepository.delete(uuid);
        boolean actual = testRepository.findById(uuid).isEmpty();

        // then
        assertTrue(actual);
    }
}