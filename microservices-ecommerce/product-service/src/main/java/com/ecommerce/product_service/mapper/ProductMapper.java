package com.ecommerce.product_service.mapper;

import com.ecommerce.product_service.dto.ProductRequestDTO;
import com.ecommerce.product_service.dto.ProductResponseDTO;
import com.ecommerce.product_service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

// Para inyectarlo con @Autowired o en el constructor
@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product toProduct(ProductRequestDTO productRequestDTO);

    ProductResponseDTO toProductResponseDTO(Product product);

    // Copiar los datos del request al product (@MappingTarget)
    @Mapping(target = "id", ignore = true)
    void updateProduct(ProductRequestDTO productRequestDTO, @MappingTarget Product product);
}
