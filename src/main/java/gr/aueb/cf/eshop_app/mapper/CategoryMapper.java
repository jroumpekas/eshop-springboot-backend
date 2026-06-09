package gr.aueb.cf.eshop_app.mapper;

import gr.aueb.cf.eshop_app.dto.CategoryInsertDTO;
import gr.aueb.cf.eshop_app.dto.CategoryReadOnlyDTO;
import gr.aueb.cf.eshop_app.dto.CategoryUpdateDTO;
import gr.aueb.cf.eshop_app.models.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category mapToCategory(CategoryInsertDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public CategoryReadOnlyDTO mapToReadOnlyDTO(Category category) {
        return CategoryReadOnlyDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public void updateCategoryFromDTO(Category category, CategoryUpdateDTO dto) {
        category.setName(dto.getName());
    }
}
