package uz.cherry.cherry_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.cherry.cherry_server.entity.Category;
import uz.cherry.cherry_server.entity.Manufacturer;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {

    boolean existsByNameAndActive(String name, boolean active);
Optional<Manufacturer> findByIdAndActive(Integer id, boolean active);
    List<Manufacturer> getByActive(boolean active);

    @Query(value = "select m.id, m.name, m.active, m.created_at, m.updated_at, m.created_by, m.updated_by from " +
            " manufacturer as m join product as p on m.id=p.manufacturer_id join " +
            "category as c on c.id=p.category_id where c.id=:categoryId and m.active=:active", nativeQuery = true)
    Set<Manufacturer> getByCategoryId(Integer categoryId, boolean active);

    @Query(value = "select m.id, m.name, m.active, m.created_at, m.updated_at, m.created_by, m.updated_by from " +
            " manufacturer as m join product as p on m.id=p.manufacturer_id join " +
            "category as c on c.id=p.category_id where c.parent_category_id=:parentCategoryId and m.active=:active", nativeQuery = true)
    Set<Manufacturer> getByParentCategoryId(Integer parentCategoryId, boolean active);



}
