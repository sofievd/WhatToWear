package se.sofievd.backend.outfit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothingRepository extends JpaRepository<Clothing, Integer> {
}
