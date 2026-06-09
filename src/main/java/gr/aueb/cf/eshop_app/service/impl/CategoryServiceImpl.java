package gr.aueb.cf.eshop_app.service.impl;

import gr.aueb.cf.eshop_app.dto.CategoryInsertDTO;
import gr.aueb.cf.eshop_app.dto.CategoryReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.CategoryUpdateDTO;
import gr.aueb.cf.eshop_app.mapper.CategoryMapper;
import gr.aueb.cf.eshop_app.models.Category;
import gr.aueb.cf.eshop_app.repository.CategoryRepository;
import gr.aueb.cf.eshop_app.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryReadOnlyDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapToReadOnlyDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryReadOnlyDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " was not found"));

        return categoryMapper.mapToReadOnlyDTO(category);
    }

    @Override
    public CategoryReadOnlyDTO createCategory(CategoryInsertDTO dto) {
        Category category = categoryMapper.mapToCategory(dto);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.mapToReadOnlyDTO(savedCategory);
    }

    @Override
    public CategoryReadOnlyDTO updateCategory(Long id, CategoryUpdateDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " was not found"));

        categoryMapper.updateCategoryFromDTO(category, dto);

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.mapToReadOnlyDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id " + id + " was not found");
        }

        categoryRepository.deleteById(id);
    }
}