package gr.aueb.cf.eshop_app.repository;

import gr.aueb.cf.eshop_app.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID; // <-- Προσθήκη import

public interface CategoryRepository extends JpaRepository<Category, UUID> { // <-- Αλλαγή από Long σε UUID

    Optional<Category> findByName(String name);

    boolean existsByName(String name);
}