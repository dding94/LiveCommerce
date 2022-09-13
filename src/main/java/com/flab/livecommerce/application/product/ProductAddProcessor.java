package com.flab.livecommerce.application.product;

import com.flab.livecommerce.domain.product.Product;
import com.flab.livecommerce.domain.product.ProductRepository;
import com.flab.livecommerce.presentation.product.request.ProductRequest;

public class ProductAddProcessor {

    private final ProductRepository repository;

    public ProductAddProcessor(ProductRepository repository) {
        this.repository = repository;
    }

    public void execute(ProductRequest requestDto) {
        repository.save(
            Product.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .salesPrice(requestDto.getSalesPrice())
                .stockQuantity(requestDto.getStockQuantity())
                .modelNumber(requestDto.getModelNumber())
                .optionGroups(requestDto.getOptionGroups())
                .build());
    }
}
