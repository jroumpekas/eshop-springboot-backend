package gr.aueb.cf.eshop_app.service;

import gr.aueb.cf.eshop_app.dto.CategoryInsertDTO;
import gr.aueb.cf.eshop_app.dto.CategoryReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.CategoryUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<CategoryReadOnlyDTO> getAllCategories();

    CategoryReadOnlyDTO getCategoryById(UUID id);

    CategoryReadOnlyDTO createCategory(CategoryInsertDTO dto);

    CategoryReadOnlyDTO updateCategory(UUID id, CategoryUpdateDTO dto);

    void deleteCategory(UUID id);
}