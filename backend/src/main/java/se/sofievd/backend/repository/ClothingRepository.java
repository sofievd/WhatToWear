package se.sofievd.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofievd.backend.enitiy.Clothing;

public interface ClothingRepository extends JpaRepository<Clothing, Integer> {
}
