package gr.aueb.cf.eshop_app.mapper;

import gr.aueb.cf.eshop_app.dto.ProductInsertDTO;
import gr.aueb.cf.eshop_app.dto.ProductReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.ProductUpdateDTO;
import gr.aueb.cf.eshop_app.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductReadOnlyDTO mapToReadOnlyDTO(Product product) {
        return new ProductReadOnlyDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                product.getOldPrice(),
                product.getCategory(),
                product.getRating()
        );
    }

    public Product mapToProduct(ProductInsertDTO dto) {
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .imageUrl(dto.getImageUrl())
                .oldPrice(dto.getOldPrice())
                .category(dto.getCategory())
                .rating(dto.getRating())
                .build();
    }

    public void updateProductFromDTO(Product product, ProductUpdateDTO dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        product.setOldPrice(dto.getOldPrice());
        product.setCategory(dto.getCategory());
        product.setRating(dto.getRating());
    }
}