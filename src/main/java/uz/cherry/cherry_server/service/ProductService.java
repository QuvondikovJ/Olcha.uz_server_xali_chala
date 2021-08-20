package uz.cherry.cherry_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.cherry.cherry_server.entity.Category;
import uz.cherry.cherry_server.entity.Manufacturer;
import uz.cherry.cherry_server.entity.Product;
import uz.cherry.cherry_server.payload.ProductDto;
import uz.cherry.cherry_server.payload.Result;
import uz.cherry.cherry_server.repository.CategoryRepository;
import uz.cherry.cherry_server.repository.ManufacturerRepository;
import uz.cherry.cherry_server.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ManufacturerRepository manufacturerRepository;

    public Result add(ProductDto productDto) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(productDto.getCategoryId(), true);
        if (!optionalCategory.isPresent()) return new Result("Such category id not exist!", false);
        if (optionalCategory.get().getParentCategory() == null)
            return new Result("It is not possible to add products to this category!", false);
        boolean existsChildCategoryByFatherCategory = categoryRepository.existsByParentCategoryIdAndActive(productDto.getCategoryId(), true);
        if (existsChildCategoryByFatherCategory)
            return new Result("It is not possible to add products to this category, because this category has child categories!", false);
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findByIdAndActive(productDto.getManufacturerId(), true);
        if (!optionalManufacturer.isPresent()) return new Result("Such manufacturer id not exist!", false);

        boolean existsByNameAndCategoryIdAndActive =
                productRepository.existsByNameAndCategoryIdAndActive(productDto.getName(), productDto.getCategoryId(), true);
        if (existsByNameAndCategoryIdAndActive)
            return new Result("This product already added to this category!", false);

        Product product = new Product();

        if (productDto.getProductId() != null) {
            Optional<Product> optionalProduct = productRepository.findByIdAndActive(productDto.getProductId(), true);
            if (!optionalProduct.isPresent()) return new Result("Such gift product id not exist!", false);
            product.setProduct(optionalProduct.get());
        } else product.setProduct(null);

        if (productDto.getDiscount() == null) product.setDiscount(0.0);
        else product.setDiscount(productDto.getDiscount());

        if (productDto.getInstallmentPlan() == null) product.setInstallmentPlan(0.0);
        else product.setInstallmentPlan(productDto.getInstallmentPlan());

        product.setName(productDto.getName());
        product.setCategory(optionalCategory.get());
        product.setManufacturer(optionalManufacturer.get());
        product.setPrice(productDto.getPrice());
        product.setAboutProduct(productDto.getAboutProduct());
        productRepository.save(product);
        return new Result("New product successfully saved.", true);
    }


    public Result getById(Integer id) {
        Optional<Product> optionalProduct = productRepository.findByIdAndActive(id, true);
        return optionalProduct.map(product -> new Result(product, true)).orElseGet(() -> new Result("Such product id not exist!", false));
    }


    /* Agar sort ni qiymati 0 bo'lsa aloqadorligi bo'yicha sort qilayotgan bo'lamiz, agar qiymati 1 bo'lsa yangi qo'shilganlari bo'yicha sort
     *  qilayotgan bo'lamiz, 2 bo'lsa price bo'yicha sort qilinayotgn bo'ladi  */
    public Result getByCategoryId(Integer categoryId, int page, int sort) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(categoryId, true);
        if (!optionalCategory.isPresent()) return new Result("Such category id not exist!", false);
        Category category = optionalCategory.get();
        if (category.getParentCategory() == null)
            return new Result("It is not possible to add products to this category, so there will be no products in this category!", false);

        boolean existsChildCategoryByParentCategory = categoryRepository.existsByParentCategoryIdAndActive(categoryId, true);

        if (existsChildCategoryByParentCategory) {
            boolean existsByCategoryIdAndActive = productRepository.existsByCategory_ParentCategoryIdAndActive(categoryId, true);
            if (!existsByCategoryIdAndActive)
                return new Result("No products have been added to this category yet!", false);
            Pageable pageable = PageRequest.of(page, 12);
            Page<Product> products;
            if (sort == 0)
                products = productRepository.findAllByCategory_ParentCategoryIdAndActiveOrderByCategoryAsc(categoryId, true, pageable);
            else if (sort == 1)
                products = productRepository.findAllByCategory_ParentCategoryIdAndActiveOrderByCreatedAtDesc(categoryId, true, pageable);
            else
                products = productRepository.findAllByCategory_ParentCategoryIdAndActiveOrderByPriceAsc(categoryId, true, pageable);
            return new Result(products, true);
        } else {
            boolean existsByCategoryIdAndActive = productRepository.existsByCategoryIdAndActive(categoryId, true);
            if (!existsByCategoryIdAndActive)
                return new Result("No products have been added to this category yet!", false);
            Pageable pageable = PageRequest.of(page, 12);
            Page<Product> products;
            if (sort == 0)
                products = productRepository.findAllByCategoryIdAndActiveOrderByCategoryAsc(categoryId, true, pageable);
            else if (sort == 1)
                products = productRepository.findAllByCategoryIdAndActiveOrderByCreatedAtDesc(categoryId, true, pageable);
            else products = productRepository.findAllByCategoryIdAndActiveOrderByPriceAsc(categoryId, true, pageable);

            return new Result(products, true);
        }
    }


    /* Xarakteristika bo'yicha olish metodi yozilishi kerak  */


    /* Agar sort ni qiymati 0 bo'lsa aloqadorligi bo'yicha sort qilayotgan bo'lamiz, agar qiymati 1 bo'lsa yangi qo'shilganlari bo'yicha sort
     *  qilayotgan bo'lamiz, 2 bo'lsa price bo'yicha sort qilinayotgn bo'ladi  */
    public Result getByDiscount(int page, int sort) {
        boolean existsByDiscountAndActive = productRepository.existsByDiscountIsNotAndActive(0.0, true);
        if (!existsByDiscountAndActive) return new Result("No discount products added yet!", false);
        Pageable pageable = PageRequest.of(page, 16);
        Page<Product> products;
        if (sort == 0)
            products = productRepository.findAllByDiscountIsNotAndActiveOrderByCategoryAsc(0.0, true, pageable);
        else if (sort == 1)
            products = productRepository.findAllByDiscountIsNotAndActiveOrderByCreatedAtDesc(0.0, true, pageable);
        else products = productRepository.findAllByDiscountIsNotAndActiveOrderByPriceAsc(0.0, true, pageable);
        return new Result(products, true);
    }


    /* Agar sort ni qiymati 0 bo'lsa aloqadorligi bo'yicha sort qilayotgan bo'lamiz, agar qiymati 1 bo'lsa yangi qo'shilganlari bo'yicha sort
     *  qilayotgan bo'lamiz, 2 bo'lsa price bo'yicha sort qilinayotgn bo'ladi  */
    public Result getByInstallmentPlan(int page, int sort) {
        boolean existsByInstallmentPlanAndActive = productRepository.existsByInstallmentPlanIsNotAndActive(0.0, true);
        if (!existsByInstallmentPlanAndActive) return new Result("No installment plan products added yet!", false);
        Pageable pageable = PageRequest.of(page, 16);
        Page<Product> products;
        if (sort == 0)
            products = productRepository.findAllByInstallmentPlanIsNotAndActiveOrderByCategoryAsc(0.0, true, pageable);
        else if (sort == 1)
            products = productRepository.findAllByInstallmentPlanIsNotAndActiveOrderByCreatedAtDesc(0.0, true, pageable);
        else products = productRepository.findAllByInstallmentPlanIsNotAndActiveOrderByPriceAsc(0.0, true, pageable);
        return new Result(products, true);
    }


    public Result getByInstallmentPlanAndCategoryId(Integer categoryId, int page, int sort) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(categoryId, true);
        if (!optionalCategory.isPresent()) return new Result("Such category id not exist!", false);

        if (optionalCategory.get().getParentCategory() == null)
            return new Result("It is not possible to add products to this category, so there will be no products in this category!", false);

        boolean existsChildCategoryByParentCategory = categoryRepository.existsByParentCategoryIdAndActive(categoryId, true);

        Pageable pageable = PageRequest.of(page, 16);
        Page<Product> products;
        if (existsChildCategoryByParentCategory) {
            boolean existsByParentCategoryIdAndInstallmentPlanAndActive = productRepository.
                    existsByInstallmentPlanIsNotAndCategory_ParentCategoryIdAndActive(0.0, categoryId, true);
            if (!existsByParentCategoryIdAndInstallmentPlanAndActive)
                return new Result("There are no products  with a installment payment in this category!", false);
            if (sort == 0)
                products = productRepository.findAllByInstallmentPlanIsNotAndCategory_ParentCategoryIdAndActiveOrderByCategoryAsc(0.0, categoryId, true, pageable);
            else if (sort == 1)
                products = productRepository.findAllByInstallmentPlanIsNotAndCategory_ParentCategoryIdAndActiveOrderByCreatedAtDesc(0.0, categoryId, true, pageable);
            else
                products = productRepository.findAllByInstallmentPlanIsNotAndCategory_ParentCategoryIdAndActiveOrderByPriceAsc(0.0, categoryId, true, pageable);
        } else {
            boolean existsByCategoryIdAndInstallmentPlanAndActive = productRepository.existsByInstallmentPlanIsNotAndCategoryIdAndActive(0.0, categoryId, true);
            if (!existsByCategoryIdAndInstallmentPlanAndActive)
                return new Result("There are no products  with a installment payment in this category!", false);
            if (sort == 0)
                products = productRepository.findAllByInstallmentPlanIsNotAndCategoryIdAndActiveOrderByCategoryAsc(0.0, categoryId, true, pageable);
            else if (sort == 1)
                products = productRepository.findAllByInstallmentPlanIsNotAndCategoryIdAndActiveOrderByCreatedAtDesc(0.0, categoryId, true, pageable);
            else
                products = productRepository.findAllByInstallmentPlanIsNotAndCategoryIdAndActiveOrderByPriceAsc(0.0, categoryId, true, pageable);
        }
        return new Result(products, true);
    }


    public Result getByDiscountAndCategoryId(Integer categoryId, int page, int sort) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(categoryId, true);
        if (!optionalCategory.isPresent()) return new Result("Such product id not exist!", false);

        if (optionalCategory.get().getParentCategory() == null)
            return new Result("It is not possible to add products to this category, so there will be no products in this category!", false);

        boolean existsChildCategoryByParentCategory = categoryRepository.existsByParentCategoryIdAndActive(categoryId, true);

        Pageable pageable = PageRequest.of(page, 16);
        Page<Product> products;
        if (existsChildCategoryByParentCategory) {
            boolean existsParentCategoryAndDiscountAndActive = productRepository.
                    existsByDiscountIsNotAndCategory_ParentCategoryIdAndActive(0.0, categoryId, true);
            if (!existsParentCategoryAndDiscountAndActive)
                return new Result("There are no products  with a discount in this category!", false);
            if (sort == 0)
                products = productRepository.findAllByDiscountIsNotAndCategory_ParentCategoryIdAndActiveOrderByCategoryAsc(0.0, categoryId, true, pageable);
            else if (sort == 1)
                products = productRepository.findAllByDiscountIsNotAndCategory_ParentCategoryIdAndActiveOrderByCreatedAtDesc(0.0, categoryId, true, pageable);
            else
                products = productRepository.findAllByDiscountIsNotAndCategory_ParentCategoryIdAndActiveOrderByPriceAsc(0.0, categoryId, true, pageable);
        } else {
            boolean existsByCategoryAndDiscountActive = productRepository.existsByDiscountIsNotAndCategoryIdAndActive(0.0, categoryId, true);
            if (!existsByCategoryAndDiscountActive)
                return new Result("There are no products  with a discount in this category!", false);
            if (sort == 0)
                products = productRepository.findAllByDiscountIsNotAndCategoryIdAndActiveOrderByCategoryAsc(0.0, categoryId, true, pageable);
            else if (sort == 1)
                products = productRepository.findAllByDiscountIsNotAndCategoryIdAndActiveOrderByCreatedAtDesc(0.0, categoryId, true, pageable);
            else
                products = productRepository.findAllByDiscountIsNotAndCategoryIdAndActiveOrderByPriceAsc(0.0, categoryId, true, pageable);
        }
        return new Result(products, true);
    }


    public Result getByCategoryIdAndManufacturerId(Integer categoryId, Integer manufacturerId, int page) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(categoryId, true);
        if (!optionalCategory.isPresent()) return new Result("Such category id not exist!", false);
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findByIdAndActive(manufacturerId, true);
        if (!optionalManufacturer.isPresent()) return new Result("Such manufacturer id not exist!", false);

        if (optionalCategory.get().getParentCategory() == null)
            return new Result("It is not possible to add products to this category, so there will be no products in this category!", false);

        Category category = optionalCategory.get();
        boolean existsChildCategoriesByParentCategory = categoryRepository.existsByParentCategoryIdAndActive(categoryId, true);
        if (existsChildCategoriesByParentCategory || category.getParentCategory().getParentCategory() != null)
            return new Result("The manufacturer page for this category does not open!", false);

        Pageable pageable = PageRequest.of(page, 8);
        Page<Product> productsOfManufacturer = productRepository.
                findAllByCategoryIdAndManufacturerIdAndActive(categoryId, manufacturerId, true, pageable);
        if (productsOfManufacturer.getTotalElements() == 0)
            return new Result("This manufacturer's product is not available in this category!", false);
        return new Result(productsOfManufacturer, true);
    }

    public Result edit(Integer id, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findByIdAndActive(id, true);
        if (!optionalProduct.isPresent()) return new Result("Such product id not exist!", false);

        Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(productDto.getCategoryId(), true);
        if (!optionalCategory.isPresent()) return new Result("Such category id not exist!", false);
        if (optionalCategory.get().getParentCategory() == null)
            return new Result("It is not possible to add products to this category!", false);
        boolean existsChildCategoryByFatherCategory = categoryRepository.existsByParentCategoryIdAndActive(productDto.getCategoryId(), true);
        if (existsChildCategoryByFatherCategory)
            return new Result("It is not possible to add products to this category because this category has child categories!", false);
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findByIdAndActive(productDto.getManufacturerId(), true);
        if (!optionalManufacturer.isPresent()) return new Result("Such manufacturer id not exist!", false);

        boolean existsByCategoryIdAndNameAndActive = productRepository.
                existsByCategoryIdAndNameAndActiveAndIdNot(productDto.getCategoryId(), productDto.getName(), true, id);
        if (existsByCategoryIdAndNameAndActive)
            return new Result("This category already has such name of product!", false);

        Product product = optionalProduct.get();

        if (productDto.getProductId() != null) {
            Optional<Product> optionalGiftProduct = productRepository.findByIdAndActive(productDto.getProductId(), true);
            if (!optionalGiftProduct.isPresent()) return new Result("Such gift product id not exist!", false);
            product.setProduct(optionalGiftProduct.get());
        } else product.setProduct(null);

        if (productDto.getDiscount() == null) product.setDiscount(0.0);
        else product.setDiscount(productDto.getDiscount());

        if (productDto.getInstallmentPlan() == null) product.setInstallmentPlan(0.0);
        else product.setInstallmentPlan(productDto.getInstallmentPlan());

        product.setName(productDto.getName());
        product.setAboutProduct(productDto.getAboutProduct());
        product.setPrice(productDto.getPrice());
        product.setManufacturer(optionalManufacturer.get());
        product.setCategory(optionalCategory.get());
        productRepository.save(product);
        return new Result("Given product successfully edited.", true);
    }

    public Result editIsFastDelivery(Integer id) {
        Optional<Product> optionalProduct = productRepository.findByIdAndActive(id, true);
        if (!optionalProduct.isPresent()) return new Result("such product id not exist!", false);
        Product product = optionalProduct.get();
        product.setFasterDelivery(!product.isFasterDelivery());
        productRepository.save(product);
        return new Result("Is fast delivery of product successfully edited.", true);
    }


    public Result delete(Integer id) {
        Optional<Product> optionalProduct = productRepository.findByIdAndActive(id, true);
        if (!optionalProduct.isPresent()) return new Result("Such product id not exist!", false);
        Product product = optionalProduct.get();
        product.setActive(false);
        productRepository.save(product);
        return new Result("Given product successfully deleted.", true);
    }

}
