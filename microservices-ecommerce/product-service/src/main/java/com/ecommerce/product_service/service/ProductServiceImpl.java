package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductRequestDTO;
import com.ecommerce.product_service.dto.ProductResponseDTO;
import com.ecommerce.product_service.mapper.ProductMapper;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = productMapper.toProduct(productRequestDTO);
        Product saveProduct = repository.save(product);

        return productMapper.toProductResponseDTO(saveProduct);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = repository.findAll();

        return products.stream().map(productMapper::toProductResponseDTO).toList();
    }

    @Override
    public ProductResponseDTO getProductById(String id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new RuntimeException("product not found: " + id)
        );
        return productMapper.toProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(String id, ProductRequestDTO productRequestDTO) {
        Product product = repository.findById(id).orElseThrow(
                () -> new RuntimeException("product not found: " + id)
        );

        productMapper.updateProduct(productRequestDTO, product);
        Product updatedProduct = repository.save(product);

        return productMapper.toProductResponseDTO(updatedProduct);
    }

    @Override
    public void deleteProductById(String id) {
        if (!repository.existsById(id)) throw new RuntimeException("product not found: " + id);

        repository.deleteById(id);
    }
}
