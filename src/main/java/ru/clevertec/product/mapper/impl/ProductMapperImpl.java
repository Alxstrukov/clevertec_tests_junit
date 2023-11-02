package ru.clevertec.product.mapper.impl;

import ru.clevertec.product.data.InfoProductDto;
import ru.clevertec.product.data.ProductDto;
import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.InvalidProductException;
import ru.clevertec.product.mapper.ProductMapper;
import ru.clevertec.util.ProductValidator;

public class ProductMapperImpl implements ProductMapper {

    ProductValidator validator = new ProductValidator();

    @Override
    public Product toProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.name())
                .setDescription(productDto.description())
                .setPrice(productDto.price());
        if (!validator.isValidateBeforeSaveProduct(product)) throw new InvalidProductException();
        return product;
    }

    @Override
    public InfoProductDto toInfoProductDto(Product product) {
        if (!validator.isValidateProduct(product)) throw new InvalidProductException();
        return new InfoProductDto(
                product.getUuid(),
                product.getName(),
                product.getDescription(),
                product.getPrice());
    }

    @Override
    public Product merge(Product product, ProductDto productDto) {
        product.setName(productDto.name())
                .setDescription(productDto.description())
                .setPrice(productDto.price());
        if (!validator.isValidateBeforeSaveProduct(product)) throw new InvalidProductException();
        return product;
    }
}
