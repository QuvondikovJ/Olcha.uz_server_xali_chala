package uz.cherry.cherry_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.cherry.cherry_server.controller.CharDepartmentController;
import uz.cherry.cherry_server.entity.CharacteristicDepartment;

import java.util.List;

public interface CharDepartmentRepository extends JpaRepository<CharacteristicDepartment, Integer> {

    boolean existsByNameAndActive(String name, boolean active);

    List<CharacteristicDepartment> findAllByActive(boolean active);

    List<CharacteristicDepartment> findAllByProductIdAndActive(Integer product_id, boolean active);

    @Query(value = "select charac.id, charac.name, charac.prodcut_id, charac.created_at, charac.updated_at, charac.created_by, charac.updated_by, charac.active from characteristic_department as charac join product as pr on charac.product_id=pr.id join " +
            " category as cat on cat.id=pr.category_id where cat.id=:categoryId and cat.active=true and pr.active=true and charac.active=true", nativeQuery = true)
    List<CharacteristicDepartment> getByCategory(Integer categoryId);

}
