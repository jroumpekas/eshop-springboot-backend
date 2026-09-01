package gr.aueb.cf.eshop_app.controller;

import gr.aueb.cf.eshop_app.dto.ProductInsertDTO;
import gr.aueb.cf.eshop_app.dto.ProductReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.ProductUpdateDTO;
import gr.aueb.cf.eshop_app.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 1. Επιστρέφει ΟΛΑ τα προϊόντα (αυτό καλεί η Angular στη σελίδα των 12 προϊόντων)
    @GetMapping
    public ResponseEntity<List<ProductReadOnlyDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 2. Επιστρέφει προϊόν με βάση το UUID
    @GetMapping("/{id}")
    public ResponseEntity<ProductReadOnlyDTO> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // 3. Επιστρέφει τα προϊόντα με Σελιδοποίηση (Paged)
    @GetMapping("/paged")
    public ResponseEntity<Page<ProductReadOnlyDTO>> getPaginatedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "name") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<ProductReadOnlyDTO> productsPage = productService.getPaginatedProducts(pageable);
        return ResponseEntity.ok(productsPage);
    }

    @PostMapping
    public ResponseEntity<ProductReadOnlyDTO> createProduct(
            @Valid @RequestBody ProductInsertDTO dto
    ) {
        ProductReadOnlyDTO product = productService.createProduct(dto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductReadOnlyDTO> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductUpdateDTO dto
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}