package gr.aueb.cf.eshop_app.service.impl;

import gr.aueb.cf.eshop_app.dto.ProductInsertDTO;
import gr.aueb.cf.eshop_app.dto.ProductReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.ProductUpdateDTO;
import gr.aueb.cf.eshop_app.exception.custom.ResourceNotFoundException;
import gr.aueb.cf.eshop_app.mapper.ProductMapper;
import gr.aueb.cf.eshop_app.models.Product;
import gr.aueb.cf.eshop_app.repository.ProductRepository;
import gr.aueb.cf.eshop_app.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductReadOnlyDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::mapToReadOnlyDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductReadOnlyDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " was not found"

                ));

        return productMapper.mapToReadOnlyDTO(product);
    }

    @Override
    public ProductReadOnlyDTO createProduct(ProductInsertDTO dto) {
        Product product = productMapper.mapToProduct(dto);

        Product savedProduct = productRepository.save(product);

        return productMapper.mapToReadOnlyDTO(savedProduct);
    }

    @Override
    public ProductReadOnlyDTO updateProduct(Long id, ProductUpdateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " was not found"

                ));

        productMapper.updateProductFromDTO(product, dto);

        Product updatedProduct = productRepository.save(product);

        return productMapper.mapToReadOnlyDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product with id " + id + " was not found");
        }

        productRepository.deleteById(id);
    }
}