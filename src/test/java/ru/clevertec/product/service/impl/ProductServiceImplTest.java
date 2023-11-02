package ru.clevertec.product.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.product.repository.ProductRepository;
import ru.clevertec.product.builders.InMemoryProductRepositoryTestData;
import ru.clevertec.product.builders.ProductTestData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private static final UUID PRODUCT_UUID = UUID.fromString("a28fed6a-f58b-47d2-bcb6-4d89c77ea0ed");

    @Mock
    ProductRepository repositoryMock;

    @Mock
    ProductMapper mapperMock;

    @InjectMocks
    ProductServiceImpl productService;

    @Captor
    ArgumentCaptor<Product> productCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidCaptor;

    @Test
    void getShouldReturnInfoProductDto() {
        // given
        InfoProductDto expected = ProductTestData.builder()
                .withUuid(PRODUCT_UUID)
                .build().createInfoProductDto();

        Product product = ProductTestData.builder()
                .withUuid(PRODUCT_UUID)
                .build().createProduct();

        when(repositoryMock.findById(PRODUCT_UUID)).thenReturn(Optional.of(product));
        when(mapperMock.toInfoProductDto(product)).thenReturn(expected);

        // when
        InfoProductDto actual = productService.get(PRODUCT_UUID);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAllShouldReturnListInfoProductDto() {
        // given
        List<Product> products = InMemoryProductRepositoryTestData.builder()
                .build().getProducts();
        List<InfoProductDto> expected = InMemoryProductRepositoryTestData.builder()
                .build().getInfoProducts();

        when(repositoryMock.findAll())
                .thenReturn(products);
        when(mapperMock.toInfoProductDto(products.get(0)))
                .thenReturn(expected.get(0));
        when(mapperMock.toInfoProductDto(products.get(1)))
                .thenReturn(expected.get(1));
        when(mapperMock.toInfoProductDto(products.get(2)))
                .thenReturn(expected.get(2));

        // when
        List<InfoProductDto> actual = productService.getAll();

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAllShouldInvokeRepositoryAndMapper() {
        // given
        List<Product> products = InMemoryProductRepositoryTestData.builder()
                .build().getProducts();
        List<InfoProductDto> infoProducts = InMemoryProductRepositoryTestData.builder()
                .build().getInfoProducts();

        when(repositoryMock.findAll())
                .thenReturn(products);
        when(mapperMock.toInfoProductDto(products.get(0)))
                .thenReturn(infoProducts.get(0));
        when(mapperMock.toInfoProductDto(products.get(1)))
                .thenReturn(infoProducts.get(1));
        when(mapperMock.toInfoProductDto(products.get(2)))
                .thenReturn(infoProducts.get(2));

        // when
        productService.getAll();

        // then
        verify(repositoryMock).findAll();
        verify(mapperMock).toInfoProductDto(products.get(0));
        verify(mapperMock).toInfoProductDto(products.get(1));
        verify(mapperMock).toInfoProductDto(products.get(2));
    }

    @Test
    void getAllShouldEqualsThreeArgumentsTransferInMapper() {
        // given
        List<Product> expected = InMemoryProductRepositoryTestData.builder()
                .build().getProducts();
        List<InfoProductDto> infoProducts = InMemoryProductRepositoryTestData.builder()
                .build().getInfoProducts();

        when(repositoryMock.findAll())
                .thenReturn(expected);
        when(mapperMock.toInfoProductDto(expected.get(0)))
                .thenReturn(infoProducts.get(0));
        when(mapperMock.toInfoProductDto(expected.get(1)))
                .thenReturn(infoProducts.get(1));
        when(mapperMock.toInfoProductDto(expected.get(2)))
                .thenReturn(infoProducts.get(2));

        // when
        productService.getAll();
        verify(mapperMock, times(3)).toInfoProductDto(productCaptor.capture());
        List<Product> actual = productCaptor.getAllValues();

        // then
        assertEquals(expected, actual);
    }


    @Test
    void createShouldInvokeRepositoryWhenProductUuidNull() {

        // given
        ProductDto productDto = ProductTestData.builder().build().createProductDto();

        Product savingProduct = ProductTestData.builder()
                .withUuid(null)
                .withCreated(null)
                .build().createProduct();

        Product savedProduct = ProductTestData.builder()
                .withName("Груша")
                .withDescription("Сладкая")
                .build().createProduct();

        when(mapperMock.toProduct(productDto))
                .thenReturn(savingProduct);
        when(repositoryMock.save(savingProduct))
                .thenReturn(savedProduct);

        // when
        productService.create(productDto);

        // then
        verify(mapperMock).toProduct(productDto);
        verify(repositoryMock).save(productCaptor.capture());

        UUID actual = productCaptor.getValue().getUuid();

        assertNull(actual);
    }


    @Test
    void updateShouldInvokeRepositoryAndMapper() {
        // given
        Product product = ProductTestData.builder()
                .withUuid(PRODUCT_UUID)
                .withName("Клюква")
                .withDescription("Кислая")
                .withPrice(BigDecimal.valueOf(5.5))
                .withCreated(LocalDateTime.MIN)
                .build().createProduct();

        ProductDto productDto = ProductTestData.builder()
                .withName("Клюква")
                .withDescription("Вкусная")
                .withPrice(BigDecimal.valueOf(4.5))
                .build().createProductDto();

        doReturn(Optional.of(product)).when(repositoryMock).findById(PRODUCT_UUID);
        doReturn(product).when(mapperMock).merge(product, productDto);
        doReturn(product).when(repositoryMock).save(product);

        // when
        productService.update(PRODUCT_UUID, productDto);

        // then
        verify(repositoryMock).findById(PRODUCT_UUID);
        verify(mapperMock).merge(product, productDto);
        verify(repositoryMock).save(product);
    }

    @ParameterizedTest
    @CsvSource(value = "7b2119fd-3d14-4a0b-9761-002c48d77368")
    void deleteShouldRunMethodInRepositoryDelete(UUID uuid) {

        // when
        productService.delete(uuid);

        // then
        verify(repositoryMock).delete(uuid);
    }

    @ParameterizedTest
    @CsvSource(value = "7b2119fd-3d14-4a0b-9761-002c48d77368")
    void deleteShouldTransferUuidInRepositoryMethod(UUID expected) {

        // when
        productService.delete(expected);

        verify(repositoryMock).delete(uuidCaptor.capture());

        UUID actual = uuidCaptor.getValue();

        // then
        assertEquals(expected, actual);
    }
}