package gr.aueb.cf.eshop_app.service;

import gr.aueb.cf.eshop_app.dto.ProductInsertDTO;
import gr.aueb.cf.eshop_app.dto.ProductReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductService {

    List<ProductReadOnlyDTO> getAllProducts();

    ProductReadOnlyDTO getProductById(Long id);

    ProductReadOnlyDTO createProduct(ProductInsertDTO dto);

    ProductReadOnlyDTO updateProduct(Long id, ProductUpdateDTO dto);

    void deleteProduct(Long id);

    Page<ProductReadOnlyDTO> getPaginatedProducts(Pageable pageable);
}