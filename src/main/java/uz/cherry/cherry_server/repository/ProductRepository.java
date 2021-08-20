package uz.cherry.cherry_server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.cherry.cherry_server.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByIdAndActive(Integer id, boolean active);

    boolean existsByNameAndCategoryIdAndActive(String name, Integer category_id, boolean active);

    boolean existsByCategoryIdAndActive(Integer category_id, boolean active);


    /* Price bo'yicha sortlash */
    Page<Product> findAllByCategoryIdAndActiveOrderByPriceAsc(Integer category_id, boolean active, Pageable pageable);

    /* Yangiligi bo'yicha sortlash */
    Page<Product> findAllByCategoryIdAndActiveOrderByCreatedAtDesc(Integer category_id, boolean active, Pageable pageable);

    /* Aloqadorligi bo'yicha sortlash */
    Page<Product> findAllByCategoryIdAndActiveOrderByCategoryAsc(Integer category_id, boolean active, Pageable pageable);

    /* Price bo'yicha sortlash */
    Page<Product> findAllByCategory_ParentCategoryIdAndActiveOrderByPriceAsc(Integer category_parentCategory_id, boolean active, Pageable pageable);

    /* Yangiligi bo'yicha sortlash */
    Page<Product> findAllByCategory_ParentCategoryIdAndActiveOrderByCreatedAtDesc(Integer category_parentCategory_id, boolean active, Pageable pageable);

    /* Aloqadorligi bo'yicha sortlash */
    Page<Product> findAllByCategory_ParentCategoryIdAndActiveOrderByCategoryAsc(Integer category_parentCategory_id, boolean active, Pageable pageable);

    boolean existsByCategory_ParentCategoryIdAndActive(Integer category_parentCategory_id, boolean active);

    boolean existsByDiscountIsNotAndActive(Double discount, boolean active);

    /* Price bo'yicha sortlash */
    Page<Product> findAllByDiscountIsNotAndActiveOrderByPriceAsc(Double discount, boolean active, Pageable pageable);

    /* Yangiligi bo'yicha sortlash */
    Page<Product> findAllByDiscountIsNotAndActiveOrderByCreatedAtDesc(Double discount, boolean active, Pageable pageable);

    /* Aloqadorligi bo'yicha sortlash */
    Page<Product> findAllByDiscountIsNotAndActiveOrderByCategoryAsc(Double discount, boolean active, Pageable pageable);

    boolean existsByInstallmentPlanIsNotAndActive(Double installmentPlan, boolean active);

    /* Price bo'yicha sortlash */
    Page<Product> findAllByInstallmentPlanIsNotAndActiveOrderByPriceAsc(Double installmentPlan, boolean active, Pageable pageable);

    /* Yangiligi bo'yicha sortlash */
    Page<Product> findAllByInstallmentPlanIsNotAndActiveOrderByCreatedAtDesc(Double installmentPlan, boolean active, Pageable pageable);

    /* Aloqadorligi bo'yicha sortlash */
    Page<Product> findAllByInstallmentPlanIsNotAndActiveOrderByCategoryAsc(Double installmentPlan, boolean active, Pageable pageable);

    /* Yangiligi bo'yicha sortlash */
    Page<Product> findAllByDiscountIsNotAndCategory_ParentCategoryIdAndActiveOrderByCreatedAtDesc(Double discount, Integer category_parentCategory_id, boolean active, Pageable pageable);

    /* Price bo'yicha sortlash */
    Page<Product> findAllByDiscountIsNotAndCategory_ParentCategoryIdAndActiveOrderByPriceAsc(Double discount, Integer category_parentCategory_id, boolean active, Pageable pageable);

    /* Aloqadorligi bo'yicha sortlash */
    Page<Product> findAllByDiscountIsNotAndCategory_ParentCategoryIdAndActiveOrderByCategoryAsc(Double discount, Integer category_parentCategory_id, boolean active, Pageable pageable);


    /* Yangiligi bo'yicha sortlash */
    Page<Product> findAllByDiscountIsNotAndCategoryIdAndActiveOrderByCreatedAtDesc(Double discount, Integer category_id, boolean active, Pageable pageable);

    /* Price bo'yicha sortlash */
    Page<Product> findAllByDiscountIsNotAndCategoryIdAndActiveOrderByPriceAsc(Double discount, Integer category_id, boolean active, Pageable pageable);

    /* Aloqadorligi bo'yicha sortlash */
    Page<Product> findAllByDiscountIsNotAndCategoryIdAndActiveOrderByCategoryAsc(Double discount, Integer category_id, boolean active, Pageable pageable);


    /* Yangiligi bo'yicha sortlash */
    Page<Product> findAllByInstallmentPlanIsNotAndCategory_ParentCategoryIdAndActiveOrderByCreatedAtDesc(Double installmentPlan, Integer category_parentCategory_id, boolean active, Pageable pageable);

    /* Price bo'yicha sortlash */
    Page<Product> findAllByInstallmentPlanIsNotAndCategory_ParentCategoryIdAndActiveOrderByPriceAsc(Double installmentPlan, Integer category_parentCategory_id, boolean active, Pageable pageable);

    /* Aloqadorligi bo'yicha sortlash */
    Page<Product> findAllByInstallmentPlanIsNotAndCategory_ParentCategoryIdAndActiveOrderByCategoryAsc(Double installmentPlan, Integer category_parentCategory_id, boolean active, Pageable pageable);


    /* Yangiligi bo'yicha sortlash */
    Page<Product> findAllByInstallmentPlanIsNotAndCategoryIdAndActiveOrderByCreatedAtDesc(Double installmentPlan, Integer category_id, boolean active, Pageable pageable);

    /* Price bo'yicha sortlash */
    Page<Product> findAllByInstallmentPlanIsNotAndCategoryIdAndActiveOrderByPriceAsc(Double installmentPlan, Integer category_id, boolean active, Pageable pageable);

    /* Aloqadorligi bo'yicha sortlash */
    Page<Product> findAllByInstallmentPlanIsNotAndCategoryIdAndActiveOrderByCategoryAsc(Double installmentPlan, Integer category_id, boolean active, Pageable pageable);

    boolean existsByInstallmentPlanIsNotAndCategoryIdAndActive(Double installmentPlan, Integer category_id, boolean active);

    boolean existsByInstallmentPlanIsNotAndCategory_ParentCategoryIdAndActive(Double installmentPlan, Integer category_parentCategory_id, boolean active);

    boolean existsByDiscountIsNotAndCategoryIdAndActive(Double discount, Integer category_id, boolean active);

    boolean existsByDiscountIsNotAndCategory_ParentCategoryIdAndActive(Double discount, Integer category_parentCategory_id, boolean active);

    Page<Product> findAllByCategoryIdAndManufacturerIdAndActive(Integer category_id, Integer manufacturer_id, boolean active, Pageable pageable);

    boolean existsByCategoryIdAndNameAndActiveAndIdNot(Integer category_id, String name, boolean active, Integer id);


}
