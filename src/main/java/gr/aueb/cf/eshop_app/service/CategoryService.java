package gr.aueb.cf.eshop_app.service;

import gr.aueb.cf.eshop_app.dto.CategoryInsertDTO;
import gr.aueb.cf.eshop_app.dto.CategoryReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.CategoryUpdateDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryReadOnlyDTO> getAllCategories();

    CategoryReadOnlyDTO getCategoryById(Long id);

    CategoryReadOnlyDTO createCategory(CategoryInsertDTO dto);

    CategoryReadOnlyDTO updateCategory(Long id, CategoryUpdateDTO dto);

    void deleteCategory(Long id);
}