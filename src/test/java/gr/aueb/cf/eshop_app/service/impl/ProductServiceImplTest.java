package gr.aueb.cf.eshop_app.service.impl;

import gr.aueb.cf.eshop_app.dto.ProductInsertDTO;
import gr.aueb.cf.eshop_app.dto.ProductReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.ProductUpdateDTO;
import gr.aueb.cf.eshop_app.mapper.ProductMapper;
import gr.aueb.cf.eshop_app.models.Product;
import gr.aueb.cf.eshop_app.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
        Product product1 = createProduct(
                1L,
                "Wireless Mouse",
                "Wireless mouse description",
                new BigDecimal("24.90"),
                10
        );

        Product product2 = createProduct(
                2L,
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
        Long productId = 1L;

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
    void getProductByIdWithUnknownIdThrowsEntityNotFoundException() {
        // Arrange
        Long productId = 99L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> productService.getProductById(productId)
        );

        // Assert
        assertEquals("Product with id 99 was not found", exception.getMessage());

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
                .name(insertDTO.getName())
                .description(insertDTO.getDescription())
                .price(insertDTO.getPrice())
                .stock(insertDTO.getStock())
                .imageUrl(insertDTO.getImageUrl())
                .oldPrice(insertDTO.getOldPrice())
                .category(insertDTO.getCategory())
                .rating(insertDTO.getRating())
                .build();

        Product savedProduct = Product.builder()
                .id(1L)
                .name(insertDTO.getName())
                .description(insertDTO.getDescription())
                .price(insertDTO.getPrice())
                .stock(insertDTO.getStock())
                .imageUrl(insertDTO.getImageUrl())
                .oldPrice(insertDTO.getOldPrice())
                .category(insertDTO.getCategory())
                .rating(insertDTO.getRating())
                .build();

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
        Long productId = 1L;

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
                updateDTO.getName(),
                updateDTO.getDescription(),
                updateDTO.getPrice(),
                updateDTO.getStock(),
                updateDTO.getImageUrl(),
                updateDTO.getOldPrice(),
                updateDTO.getCategory(),
                updateDTO.getRating()
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
    void updateProductWithUnknownIdThrowsEntityNotFoundException() {
        // Arrange
        Long productId = 99L;

        ProductUpdateDTO updateDTO = ProductUpdateDTO.builder()
                .name("Updated Mouse")
                .description("Updated description")
                .price(new BigDecimal("29.90"))
                .stock(15)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> productService.updateProduct(productId, updateDTO)
        );

        // Assert
        assertEquals("Product with id 99 was not found", exception.getMessage());

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
        verifyNoInteractions(productMapper);
    }

    @Test
    void deleteProductWithExistingIdDeletesProduct() {
        // Arrange
        Long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(true);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository).existsById(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void deleteProductWithUnknownIdThrowsEntityNotFoundException() {
        // Arrange
        Long productId = 99L;

        when(productRepository.existsById(productId)).thenReturn(false);

        // Act
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> productService.deleteProduct(productId)
        );

        // Assert
        assertEquals("Product with id 99 was not found", exception.getMessage());

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
                .name(insertDTO.getName())
                .description(insertDTO.getDescription())
                .price(insertDTO.getPrice())
                .stock(insertDTO.getStock())
                .imageUrl(insertDTO.getImageUrl())
                .oldPrice(insertDTO.getOldPrice())
                .category(insertDTO.getCategory())
                .rating(insertDTO.getRating())
                .build();

        Product savedProduct = Product.builder()
                .id(2L)
                .name(insertDTO.getName())
                .description(insertDTO.getDescription())
                .price(insertDTO.getPrice())
                .stock(insertDTO.getStock())
                .imageUrl(insertDTO.getImageUrl())
                .oldPrice(insertDTO.getOldPrice())
                .category(insertDTO.getCategory())
                .rating(insertDTO.getRating())
                .build();

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
            Long id,
            String name,
            String description,
            BigDecimal price,
            Integer stock
    ) {
        return Product.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .imageUrl("/products/test-product.webp")
                .oldPrice(new BigDecimal("49.90"))
                .category("Test Category")
                .rating(new BigDecimal("4.5"))
                .build();
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