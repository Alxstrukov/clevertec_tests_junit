package ru.clevertec.util;

import ru.clevertec.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class ProductValidator {

    public boolean isValidateProduct(Product testingProduct) {
        if (testingProduct == null) return false;
        if (!isValidateProductName(testingProduct.getName())) return false;
        if (!isValidateProductDescription(testingProduct.getDescription())) return false;
        if (!isValidateProductPrice(testingProduct.getPrice())) return false;
        return isValidateProductCreated(testingProduct.getCreated());

    }

    public boolean isValidateBeforeSaveProduct(Product testingProduct) {
        if (testingProduct == null) return false;
        if (!isValidateProductName(testingProduct.getName())) return false;
        if (!isValidateProductDescription(testingProduct.getDescription())) return false;
        return  isValidateProductPrice(testingProduct.getPrice());
    }

    private boolean isValidateProductName(String testingProductName) {
        Pattern pattern = Pattern.compile("[а-яА-Я0-9 ]{5,10}");
        if (testingProductName == null) return false;
        return testingProductName.matches(pattern.pattern());

    }

    private boolean isValidateProductDescription(String testingProductDescription) {
        Pattern pattern = Pattern.compile("[а-яА-Я0-9 ]{10,30}");
        if (testingProductDescription == null) return false;
        return testingProductDescription.matches(pattern.pattern());
    }

    private boolean isValidateProductPrice(BigDecimal testingPrice) {
        return (!(testingPrice.signum() == -1));
    }

    private boolean isValidateProductCreated(LocalDateTime testingCreated) {
        return !(testingCreated == null);
    }
}
