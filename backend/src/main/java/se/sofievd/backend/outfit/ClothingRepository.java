package se.sofievd.backend.outfit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.sofievd.backend.outfit.model.Clothing;

import java.util.List;

@Repository
public interface ClothingRepository extends JpaRepository<Clothing, Integer> {

    @Query("""
    SELECT c FROM Clothing c
    WHERE c.category = :category
    AND c.cloValue BETWEEN :minClo AND :maxClo
""")
    List<Clothing> findByCategoryAndCloValueBetween(@Param("category") String category,
                                                    @Param("minClo") Double minClo,
                                                    @Param("maxClo") Double maxClo);
}
