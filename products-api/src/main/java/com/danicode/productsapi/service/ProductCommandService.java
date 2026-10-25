package com.danicode.productsapi.service;

import com.danicode.productsapi.model.dto.ProductDto;

public interface ProductCommandService {

    void sendCreate(ProductDto dto);
}
