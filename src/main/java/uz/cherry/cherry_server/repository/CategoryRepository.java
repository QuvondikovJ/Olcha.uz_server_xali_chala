package uz.cherry.cherry_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.cherry.cherry_server.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByUrlAndActive(String url, boolean active);

    boolean existsByUrlAndActiveAndIdNot(String url, boolean active, Integer id);

    Category getByIdAndActive(Integer parentCategory_id, boolean active);

    List<Category> findAllByParentCategoryIdAndActive(Integer parentCategoryId, boolean active);

    boolean existsByParentCategoryIdAndActive(Integer parentCategory_id, boolean active);

    Optional<Category> findByIdAndActive(Integer id, boolean active);

    /*  If parent category is null  */
    boolean existsByParentCategoryAndNameAndActive(Category parentCategory, String name, boolean active);

    /*  If parent category is null for edit*/
    boolean existsByParentCategoryAndNameAndActiveAndIdNot(Category parentCategory, String name, boolean active, Integer id);

    /*  If parent category is not null  */
    boolean existsByParentCategoryIdAndNameAndActive(Integer parentCategory_id, String name, boolean active);

    /*  If parent category is not null for edit  */
    boolean existsByParentCategoryIdAndNameAndActiveAndIdNot(Integer parentCategory_id, String name, boolean active, Integer id);

    @Query(value = "select cat.id, cat.name, cat.parent_category_id, cat.url, cat.text, cat.active, cat.created_at, cat.updated_at, cat.created_by, cat.updated_by, cat.attachment_id from category as cat join product as pr on cat.id=pr.category_id where " +
            " pr.discount<>:discount and pr.active=true and cat.active=true", nativeQuery = true)
    Set<Category> getByProductDiscount(Double discount);

    @Query(value = "select cat.id, cat.name, cat.parent_category_id, cat.url, cat.text, cat.active, cat.created_at, cat.updated_at, cat.created_by, cat.updated_by, cat.attachment_id from category as cat join product as pr on cat.id=pr.category_id where " +
            " pr.discount<>:discount and pr.active=true and cat.active=true and cat.parent_category_id=:parentCategoryId", nativeQuery = true)
    Set<Category> getChildCategoriesByProductDiscountAndParentCategoryId(Double discount, Integer parentCategoryId);

    @Query(value = "select cat.id, cat.name, cat.parent_category_id, cat.url, cat.text, cat.active, cat.created_at, cat.updated_at, cat.created_by, cat.updated_by, cat.attachment_id from category as cat join product as pr on cat.id=pr.category_id where " +
            " pr.discount<>:discount and pr.active=true and cat.active=true and cat.id=:categoryId", nativeQuery = true)
    Category getByProductDiscountAndCategoryId(Double discount, Integer categoryId);

    @Query(value = "select cat.id, cat.name, cat.parent_category_id, cat.url, cat.text, cat.active, cat.created_at, cat.updated_at, cat.created_by, cat.updated_by, cat.attachment_id from category as cat join product as pr on cat.id=pr.category_id where " +
            " pr.installment_plan<>:installmentPlan and pr.active=true and cat.active=true", nativeQuery = true)
    Set<Category> getByProductInstallmentPlan(Double installmentPlan);

    @Query(value = "select cat.id, cat.name, cat.parent_category_id, cat.url, cat.text, cat.active, cat.created_at, cat.updated_at, cat.created_by, cat.updated_by, cat.attachment_id from category as cat join product as pr on cat.id=pr.category_id where " +
            " pr.installment_plan<>:installmentPlan and pr.active=true and cat.active=true and cat.parent_category_id=:parentCategoryId", nativeQuery = true)
    Set<Category> getChildCategoriesByProductInstallmentPlanAndParentCategoryId(Double installmentPlan, Integer parentCategoryId);

    @Query(value = "select cat.id, cat.name, cat.parent_category_id, cat.url, cat.text, cat.active, cat.created_at, cat.updated_at, cat.created_by, cat.updated_by, cat.attachment_id from category as cat join product as pr on cat.id=pr.category_id where " +
            " pr.installment_plan<>:installmentPlan and pr.active=true and cat.active=true and cat.id=:categoryId", nativeQuery = true)
    Category getByProductInstallmentPlanAndCategoryId(Double installmentPlan, Integer categoryId);

    @Query(value = "select cat.id, cat.name, cat.parent_category_id, cat.url, cat.text, cat.active, cat.created_at, cat.updated_at, cat.created_by, cat.updated_by, cat.attachment_id from category as cat join product as pr " +
            " on cat.id=pr.category_id join manufacturer as man on man.id=pr.manufacturer_id where man.id=:manufacturerId and cat.active=true and pr.active=true", nativeQuery = true)
    Set<Category> getByManufacturerId(Integer manufacturerId);


}