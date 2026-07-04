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
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .stock(dto.stock())
                .imageUrl(dto.imageUrl())
                .oldPrice(dto.oldPrice())
                .category(dto.category())
                .rating(dto.rating())
                .build();
    }

    public void updateProductFromDTO(Product product, ProductUpdateDTO dto) {
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());
        product.setImageUrl(dto.imageUrl());
        product.setOldPrice(dto.oldPrice());
        product.setCategory(dto.category());
        product.setRating(dto.rating());
    }
}