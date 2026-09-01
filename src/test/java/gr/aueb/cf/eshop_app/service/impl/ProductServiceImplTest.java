package gr.aueb.cf.eshop_app.service.impl;

import gr.aueb.cf.eshop_app.dto.ProductInsertDTO;
import gr.aueb.cf.eshop_app.dto.ProductReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.ProductUpdateDTO;
import gr.aueb.cf.eshop_app.mapper.ProductMapper;
import gr.aueb.cf.eshop_app.models.Product;
import gr.aueb.cf.eshop_app.repository.ProductRepository;
import gr.aueb.cf.eshop_app.exception.custom.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getAllProductsReturnsMappedProducts() {
        // Arrange
        UUID id1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID id2 = UUID.fromString("22222222-2222-2222-2222-222222222222");

        Product product1 = createProduct(
                id1,
                "Wireless Mouse",
                "Wireless mouse description",
                new BigDecimal("24.90"),
                10
        );

        Product product2 = createProduct(
                id2,
                "Laptop Lenovo",
                "Laptop description",
                new BigDecimal("749.99"),
                5
        );

        ProductReadOnlyDTO dto1 = createReadOnlyDTO(product1);
        ProductReadOnlyDTO dto2 = createReadOnlyDTO(product2);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        when(productMapper.mapToReadOnlyDTO(product1)).thenReturn(dto1);
        when(productMapper.mapToReadOnlyDTO(product2)).thenReturn(dto2);

        // Act
        List<ProductReadOnlyDTO> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(dto1, result.get(0));
        assertSame(dto2, result.get(1));

        verify(productRepository).findAll();
        verify(productMapper).mapToReadOnlyDTO(product1);
        verify(productMapper).mapToReadOnlyDTO(product2);
    }

    @Test
    void getProductByIdWithExistingIdReturnsProduct() {
        // Arrange
        UUID productId = UUID.randomUUID();

        Product product = createProduct(
                productId,
                "Wireless Mouse",
                "Wireless mouse description",
                new BigDecimal("24.90"),
                10
        );

        ProductReadOnlyDTO expectedDTO = createReadOnlyDTO(product);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.mapToReadOnlyDTO(product)).thenReturn(expectedDTO);

        // Act
        ProductReadOnlyDTO result = productService.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertSame(expectedDTO, result);

        verify(productRepository).findById(productId);
        verify(productMapper).mapToReadOnlyDTO(product);
    }

    @Test
    void getProductByIdWithUnknownIdThrowsResourceNotFoundException() {
        // Arrange
        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getProductById(productId)
        );

        // Assert
        assertEquals("Product with id " + productId + " was not found", exception.getMessage());

        verify(productRepository).findById(productId);
        verifyNoInteractions(productMapper);
    }

    @Test
    void createProductSavesProductAndReturnsMappedDTO() {
        // Arrange
        ProductInsertDTO insertDTO = ProductInsertDTO.builder()
                .name("Wireless Mouse")
                .description("Wireless mouse description")
                .price(new BigDecimal("24.90"))
                .stock(10)
                .imageUrl("/products/mouse.webp")
                .oldPrice(new BigDecimal("42.90"))
                .category("Accessories")
                .rating(new BigDecimal("4.7"))
                .build();

        Product productToSave = Product.builder()
                .name(insertDTO.name())
                .description(insertDTO.description())
                .price(insertDTO.price())
                .stock(insertDTO.stock())
                .imageUrl(insertDTO.imageUrl())
                .oldPrice(insertDTO.oldPrice())
                .category(insertDTO.category())
                .rating(insertDTO.rating())
                .build();

        Product savedProduct = Product.builder()
                .name(insertDTO.name())
                .description(insertDTO.description())
                .price(insertDTO.price())
                .stock(insertDTO.stock())
                .imageUrl(insertDTO.imageUrl())
                .oldPrice(insertDTO.oldPrice())
                .category(insertDTO.category())
                .rating(insertDTO.rating())
                .build();

        savedProduct.setId(UUID.randomUUID());

        ProductReadOnlyDTO expectedDTO = createReadOnlyDTO(savedProduct);

        when(productMapper.mapToProduct(insertDTO)).thenReturn(productToSave);
        when(productRepository.save(productToSave)).thenReturn(savedProduct);
        when(productMapper.mapToReadOnlyDTO(savedProduct)).thenReturn(expectedDTO);

        // Act
        ProductReadOnlyDTO result = productService.createProduct(insertDTO);

        // Assert
        assertNotNull(result);
        assertSame(expectedDTO, result);

        verify(productMapper).mapToProduct(insertDTO);
        verify(productRepository).save(productToSave);
        verify(productMapper).mapToReadOnlyDTO(savedProduct);
    }

    @Test
    void updateProductWithExistingIdUpdatesProductAndReturnsMappedDTO() {
        // Arrange
        UUID productId = UUID.randomUUID();

        Product existingProduct = createProduct(
                productId,
                "Old Mouse",
                "Old description",
                new BigDecimal("20.00"),
                5
        );

        ProductUpdateDTO updateDTO = ProductUpdateDTO.builder()
                .name("Updated Mouse")
                .description("Updated description")
                .price(new BigDecimal("29.90"))
                .stock(15)
                .imageUrl("/products/updated-mouse.webp")
                .oldPrice(new BigDecimal("39.90"))
                .category("Accessories")
                .rating(new BigDecimal("4.8"))
                .build();

        ProductReadOnlyDTO expectedDTO = new ProductReadOnlyDTO(
                productId,
                updateDTO.name(),
                updateDTO.description(),
                updateDTO.price(),
                updateDTO.stock(),
                updateDTO.imageUrl(),
                updateDTO.oldPrice(),
                updateDTO.category(),
                updateDTO.rating()
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);
        when(productMapper.mapToReadOnlyDTO(existingProduct)).thenReturn(expectedDTO);

        // Act
        ProductReadOnlyDTO result = productService.updateProduct(productId, updateDTO);

        // Assert
        assertNotNull(result);
        assertSame(expectedDTO, result);

        verify(productRepository).findById(productId);
        verify(productMapper).updateProductFromDTO(existingProduct, updateDTO);
        verify(productRepository).save(existingProduct);
        verify(productMapper).mapToReadOnlyDTO(existingProduct);
    }

    @Test
    void updateProductWithUnknownIdThrowsResourceNotFoundException() {
        // Arrange
        UUID productId = UUID.randomUUID();

        ProductUpdateDTO updateDTO = ProductUpdateDTO.builder()
                .name("Updated Mouse")
                .description("Updated description")
                .price(new BigDecimal("29.90"))
                .stock(15)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.updateProduct(productId, updateDTO)
        );

        // Assert
        assertEquals("Product with id " + productId + " was not found", exception.getMessage());

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
        verifyNoInteractions(productMapper);
    }

    @Test
    void deleteProductWithExistingIdDeletesProduct() {
        // Arrange
        UUID productId = UUID.randomUUID();

        when(productRepository.existsById(productId)).thenReturn(true);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository).existsById(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void deleteProductWithUnknownIdThrowsResourceNotFoundException() {
        // Arrange
        UUID productId = UUID.randomUUID();

        when(productRepository.existsById(productId)).thenReturn(false);

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.deleteProduct(productId)
        );

        // Assert
        assertEquals("Product with id " + productId + " was not found", exception.getMessage());

        verify(productRepository).existsById(productId);
        verify(productRepository, never()).deleteById(productId);
    }

    @Test
    void createProductPassesCorrectProductToRepository() {
        // Arrange
        ProductInsertDTO insertDTO = ProductInsertDTO.builder()
                .name("Mechanical Keyboard")
                .description("Mechanical keyboard description")
                .price(new BigDecimal("89.90"))
                .stock(20)
                .imageUrl("/products/keyboard.webp")
                .oldPrice(new BigDecimal("119.90"))
                .category("Accessories")
                .rating(new BigDecimal("4.6"))
                .build();

        Product productToSave = Product.builder()
                .name(insertDTO.name())
                .description(insertDTO.description())
                .price(insertDTO.price())
                .stock(insertDTO.stock())
                .imageUrl(insertDTO.imageUrl())
                .oldPrice(insertDTO.oldPrice())
                .category(insertDTO.category())
                .rating(insertDTO.rating())
                .build();

        Product savedProduct = Product.builder()
                .name(insertDTO.name())
                .description(insertDTO.description())
                .price(insertDTO.price())
                .stock(insertDTO.stock())
                .imageUrl(insertDTO.imageUrl())
                .oldPrice(insertDTO.oldPrice())
                .category(insertDTO.category())
                .rating(insertDTO.rating())
                .build();

        savedProduct.setId(UUID.randomUUID());

        ProductReadOnlyDTO expectedDTO = createReadOnlyDTO(savedProduct);

        when(productMapper.mapToProduct(insertDTO)).thenReturn(productToSave);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.mapToReadOnlyDTO(savedProduct)).thenReturn(expectedDTO);

        // Act
        productService.createProduct(insertDTO);

        // Assert
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(productCaptor.capture());

        Product capturedProduct = productCaptor.getValue();

        assertEquals("Mechanical Keyboard", capturedProduct.getName());
        assertEquals("Mechanical keyboard description", capturedProduct.getDescription());
        assertEquals(0, new BigDecimal("89.90").compareTo(capturedProduct.getPrice()));
        assertEquals(20, capturedProduct.getStock());
        assertEquals("/products/keyboard.webp", capturedProduct.getImageUrl());
        assertEquals(0, new BigDecimal("119.90").compareTo(capturedProduct.getOldPrice()));
        assertEquals("Accessories", capturedProduct.getCategory());
        assertEquals(0, new BigDecimal("4.6").compareTo(capturedProduct.getRating()));
    }

    private Product createProduct(
            UUID id,
            String name,
            String description,
            BigDecimal price,
            Integer stock
    ) {
        Product product = Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .imageUrl("/products/test-product.webp")
                .oldPrice(new BigDecimal("49.90"))
                .category("Test Category")
                .rating(new BigDecimal("4.5"))
                .build();

        product.setId(id);
        return product;
    }

    private ProductReadOnlyDTO createReadOnlyDTO(Product product) {
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
}