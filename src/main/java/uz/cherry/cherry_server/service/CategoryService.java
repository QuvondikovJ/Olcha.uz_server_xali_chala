package uz.cherry.cherry_server.service;

import com.sun.org.apache.bcel.internal.generic.ARETURN;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.cherry.cherry_server.entity.Attachment;
import uz.cherry.cherry_server.entity.Category;
import uz.cherry.cherry_server.entity.Manufacturer;
import uz.cherry.cherry_server.entity.Product;
import uz.cherry.cherry_server.payload.CategoryDto;
import uz.cherry.cherry_server.payload.Result;
import uz.cherry.cherry_server.repository.AttachmentRepository;
import uz.cherry.cherry_server.repository.CategoryRepository;
import uz.cherry.cherry_server.repository.ManufacturerRepository;

import java.util.*;

@Service
public class CategoryService {


    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    ManufacturerRepository manufacturerRepository;


    public Result add(CategoryDto categoryDto) {

        if (categoryDto.getParentCategoryId() == null) {
            boolean existsByParentCategoryIdAndNameAndActive =
                    categoryRepository.existsByParentCategoryAndNameAndActive(null, categoryDto.getName(), true);
            if (existsByParentCategoryIdAndNameAndActive)
                return new Result("This parent category already has such name of category!", false);
        } else {
            boolean existsByParentCategoryIdAndNameAndActive =
                    categoryRepository.existsByParentCategoryIdAndNameAndActive(categoryDto.getParentCategoryId(), categoryDto.getName(), true);
            if (existsByParentCategoryIdAndNameAndActive)
                return new Result("This parent category already has such name of category!", false);
        }

        boolean existsByUrlAndActive = categoryRepository.existsByUrlAndActive(categoryDto.getUrl(), true);
        if (existsByUrlAndActive) return new Result("This url belongs to another category!", false);

        Category category = new Category();

        if (categoryDto.getParentCategoryId() == null) category.setParentCategory(null);
        else {
            Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(categoryDto.getParentCategoryId(), true);
            if (!optionalCategory.isPresent()) return new Result("Such parent category id not exist!", false);
            category.setParentCategory(optionalCategory.get());
        }

        if (categoryDto.getAttachmentId() == null) category.setAttachment(null);
        else {
            Attachment attachment = attachmentRepository.getOne(categoryDto.getAttachmentId().get(0));
            category.setAttachment(attachment);
        }
        category.setName(categoryDto.getName());
        category.setUrl(categoryDto.getUrl());
        category.setText(categoryDto.getText());
        categoryRepository.save(category);
        return new Result("New category successfully saved.", true);
    }


    public Result getGrandCategories() {
        List<Category> categoryList = categoryRepository.findAllByParentCategoryIdAndActive(null, true);
        if (categoryList.size() > 0) return new Result(categoryList, true);
        else return new Result("There are no categories yet!", false);
    }

    public Result getGrandAndFatherChildCategories(Integer grandCategoryId) {
        Optional<Category> optionalGrandCategory = categoryRepository.findByIdAndActive(grandCategoryId, true);
        if (!optionalGrandCategory.isPresent()) return new Result("Such grand category id not exist!", false);
        if (optionalGrandCategory.get().getParentCategory() != null)
            return new Result("Please, send me a grand category id!", false);

        List<Category> grandCategories = categoryRepository.findAllByParentCategoryIdAndActive(null, true);
        List<Category> fatherCategories = categoryRepository.findAllByParentCategoryIdAndActive(grandCategoryId, true);
        if (fatherCategories.size() == 0) return new Result("This category does not have any child categories!", false);
        List<List<Category>> childCategories = new ArrayList<>();
        for (Category category : fatherCategories) {
            List<Category> childCategoryList = categoryRepository.findAllByParentCategoryIdAndActive(category.getId(), true);
            childCategories.add(childCategoryList);
        }
        List<Object> allCategories = new ArrayList<>();
        allCategories.add(grandCategories);
        allCategories.add(fatherCategories);
        allCategories.add(childCategories);
        return new Result(allCategories, true);
    }

    public Result getGrandAndFatherCategories(Integer grandCategoryId) {
        Optional<Category> optionalGrandCategory = categoryRepository.findByIdAndActive(grandCategoryId, true);
        if (!optionalGrandCategory.isPresent()) return new Result("Such grand category id not exist!", false);
        Category grandCategory = optionalGrandCategory.get();
        if (grandCategory.getParentCategory() != null)
            return new Result("Please, send me a grand category id!", false);

        List<Category> fatherCategories = categoryRepository.findAllByParentCategoryIdAndActive(grandCategoryId, true);
        if (fatherCategories.size() == 0)
            return new Result("This category does not have any father categories yet!", false);
        List<Object> grandAndFatherCategories = new ArrayList<>();
        grandAndFatherCategories.add(grandCategory);
        grandAndFatherCategories.add(fatherCategories);
        return new Result(grandAndFatherCategories, true);
    }

    /* Agar bu father bo'lsa buni grand ini va child ini olamiz, agar bu child bo'lsa buni grand ini va father ini olamiz */
    public Result getCategoryWithRelatives(Integer categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(categoryId, true);
        if (!optionalCategory.isPresent()) return new Result("Such category id not exist!", false);

        Category currentCategory = optionalCategory.get();
        if (currentCategory.getParentCategory() == null) return new Result("Please don't do this again!", false);
        List<Object> categoryWithRelatives = new ArrayList<>();

        /* If current category is father category , so this block works  */
        if (currentCategory.getParentCategory().getParentCategory() == null) {
            Category grandCategory = currentCategory.getParentCategory();
            List<Category> childCategories = categoryRepository.findAllByParentCategoryIdAndActive(categoryId, true);
            categoryWithRelatives.add(grandCategory);
            categoryWithRelatives.add(currentCategory);
            categoryWithRelatives.add(childCategories);
        } else {
            /* If current category is child category , so this block works  */
            Category grandCategory = currentCategory.getParentCategory().getParentCategory();
            Category fatherCategory = currentCategory.getParentCategory();
            categoryWithRelatives.add(grandCategory);
            categoryWithRelatives.add(fatherCategory);
            categoryWithRelatives.add(currentCategory);
        }
        return new Result(categoryWithRelatives, true);
    }

    /* Bu metod ishlaydi, qachonki discount page ga kirganda qaysi mahsulotni discounti bo'lsa shu mahsulotlarni grand va father
     *  category larini olib boradi */
    public Result getByProductDiscount() {
        Set<Category> getByProductDiscount = categoryRepository.getByProductDiscount(0.0);
        if (getByProductDiscount.size() == 0) return new Result("There are no discount products yet!", false);
        Set<Category> grandCategories = new HashSet<>();
        Set<Category> fatherCategories = new HashSet<>();

        for (Category category : getByProductDiscount) {

            if (category.getParentCategory().getParentCategory() == null) {
                grandCategories.add(category.getParentCategory());
                fatherCategories.add(category);
            } else {
                grandCategories.add(category.getParentCategory().getParentCategory());
                fatherCategories.add(category.getParentCategory());
            }
        }
        Set<Set<Category>> categories = new HashSet<>();
        categories.add(grandCategories);
        categories.add(fatherCategories);
        return new Result(categories, true);
    }


    public Result getByProductDiscountAndCategoryId(Integer categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(categoryId, true);
        if (!optionalCategory.isPresent()) return new Result("Such category id not exist!", false);

        if (optionalCategory.get().getParentCategory() == null)
            return new Result("Please, send me the father or child category id!", false);

        boolean existsChildCategoryByFatherCategoryId = categoryRepository.existsByParentCategoryIdAndActive(categoryId, true);

        Category grandCategory;
        Category fatherCategory;
        Set<Category> childCategories;
        Category currentCategory;
        List<Object> allCategories = new ArrayList<>();

        if (existsChildCategoryByFatherCategoryId) {
            /* Agar father category bo'lsa va buni child categorylari bo'lsa ishlaydi */
            childCategories = categoryRepository.getChildCategoriesByProductDiscountAndParentCategoryId(0.0, categoryId);
            if (childCategories.size() == 0)
                return new Result("There are no discount products in this category!", false);
            grandCategory = optionalCategory.get().getParentCategory();
            fatherCategory = optionalCategory.get();
            currentCategory = optionalCategory.get();

            allCategories.add(grandCategory);
            allCategories.add(fatherCategory);
            allCategories.add(childCategories);
            allCategories.add(currentCategory);
        } else if (optionalCategory.get().getParentCategory().getParentCategory() == null) {
            /* Agar father category bo'lsa va buni child categorylari bo'lmasa ishlaydi */
            Category category = categoryRepository.getByProductDiscountAndCategoryId(0.0, categoryId);
            if (category == null) return new Result("There are no discount products in this category!", false);
            allCategories.add(category.getParentCategory());
            allCategories.add(category);
        } else {
            /* Agar child category bo'lsa ishlaydi */
            Category category = categoryRepository.getByProductDiscountAndCategoryId(0.0, categoryId);
            if (category == null) return new Result("There are no discount products in this category!", false);
            childCategories = categoryRepository.getChildCategoriesByProductDiscountAndParentCategoryId(0.0, category.getParentCategory().getId());
            allCategories.add(category.getParentCategory().getParentCategory());
            allCategories.add(category.getParentCategory());
            allCategories.add(childCategories);
            allCategories.add(category);
        }
        return new Result(allCategories, true);
    }

    public Result getByInstallmentPlan() {
        Set<Category> getByInstallmentPlan = categoryRepository.getByProductInstallmentPlan(0.0);
        if (getByInstallmentPlan.size() == 0) return new Result("There are no installment plan products yet!", false);
        Set<Category> grandCategories = new HashSet<>();
        Set<Category> fatherCategories = new HashSet<>();

        for (Category category : getByInstallmentPlan) {

            if (category.getParentCategory().getParentCategory() == null) {
                grandCategories.add(category.getParentCategory());
                fatherCategories.add(category);
            } else {
                grandCategories.add(category.getParentCategory().getParentCategory());
                fatherCategories.add(category.getParentCategory());
            }
        }
        Set<Set<Category>> categories = new HashSet<>();
        categories.add(grandCategories);
        categories.add(fatherCategories);
        return new Result(categories, true);
    }

    public Result getByInstallmentPlanAndCategoryId(Integer categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(categoryId, true);
        if (!optionalCategory.isPresent()) return new Result("Such category id not exist!", false);

        if (optionalCategory.get().getParentCategory() == null)
            return new Result("Please, send me the father or child category id!", false);

        boolean existsChildCategoryByFatherCategoryId = categoryRepository.existsByParentCategoryIdAndActive(categoryId, true);

        Category grandCategory;
        Category fatherCategory;
        Set<Category> childCategories;
        Category currentCategory;
        List<Object> allCategories = new ArrayList<>();

        if (existsChildCategoryByFatherCategoryId) {
            /* Agar father category bo'lsa va buni child categorylari bo'lsa ishlaydi */
            childCategories = categoryRepository.getChildCategoriesByProductInstallmentPlanAndParentCategoryId(0.0, categoryId);
            if (childCategories.size() == 0)
                return new Result("There are no installment plan products in this category!", false);
            grandCategory = optionalCategory.get().getParentCategory();
            fatherCategory = optionalCategory.get();
            currentCategory = optionalCategory.get();

            allCategories.add(grandCategory);
            allCategories.add(fatherCategory);
            allCategories.add(childCategories);
            allCategories.add(currentCategory);
        } else if (optionalCategory.get().getParentCategory().getParentCategory() == null) {
            /* Agar father category bo'lsa va buni child categorylari bo'lmasa ishlaydi */
            Category category = categoryRepository.getByProductInstallmentPlanAndCategoryId(0.0, categoryId);
            if (category == null) return new Result("There are no installment plan products in this category!", false);
            allCategories.add(category.getParentCategory());
            allCategories.add(category);
        } else {
            /* Agar child category bo'lsa ishlaydi */
            Category category = categoryRepository.getByProductInstallmentPlanAndCategoryId(0.0, categoryId);
            if (category == null) return new Result("There are no discount products in this category!", false);
            childCategories = categoryRepository.getChildCategoriesByProductInstallmentPlanAndParentCategoryId(0.0, category.getParentCategory().getId());
            allCategories.add(category.getParentCategory().getParentCategory());
            allCategories.add(category.getParentCategory());
            allCategories.add(childCategories);
            allCategories.add(category);
        }
        return new Result(allCategories, true);
    }


    public Result getByManufacturerId(Integer manufacturerId) {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findByIdAndActive(manufacturerId, true);
        if (!optionalManufacturer.isPresent()) return new Result("Such manufacturer id not exist!", false);

        Set<Category> getByManufacturer = categoryRepository.getByManufacturerId(manufacturerId);
        if (getByManufacturer.size() == 0)
            return new Result("The product of this manufacturer is not available in the warehouse at all!", false);

        Set<Category> fatherCategoriesByManufacturer = new LinkedHashSet<>();
        for (Category category : getByManufacturer) {
            if (category.getParentCategory().getParentCategory() == null)
                fatherCategoriesByManufacturer.add(category);
        }
        if (fatherCategoriesByManufacturer.size() == 0)
            return new Result("There is no separate page for this manufacturer!", false);
        return new Result(fatherCategoriesByManufacturer, true);
    }


    public Result edit(Integer categoryId, CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(categoryId, true);
        if (!optionalCategory.isPresent()) return new Result("Such category id not exist!", false);
        Category category = optionalCategory.get();

        if (categoryDto.getParentCategoryId() == null) {
            boolean existsByParentCategoryIdAndNameAndActive =
                    categoryRepository.existsByParentCategoryAndNameAndActiveAndIdNot(null, categoryDto.getName(), true, categoryId);
            if (existsByParentCategoryIdAndNameAndActive)
                return new Result("This parent category already has such category name!", false);
            category.setParentCategory(null);
        } else {
            boolean existsByParentCategoryIdAndNameAndActive =
                    categoryRepository.existsByParentCategoryIdAndNameAndActiveAndIdNot
                            (categoryDto.getParentCategoryId(), categoryDto.getName(), true, categoryId);
            if (existsByParentCategoryIdAndNameAndActive)
                return new Result("This parent category already has such category name!", false);
            category.setParentCategory(categoryRepository.getOne(categoryDto.getParentCategoryId()));
        }

        boolean existsByUrlAndActive = categoryRepository.existsByUrlAndActiveAndIdNot(categoryDto.getUrl(), true, categoryId);
        if (existsByUrlAndActive) return new Result("This url belongs to another category!", false);

        if (categoryDto.getAttachmentId() == null) category.setAttachment(null);
        else {
            Attachment attachment = attachmentRepository.getOne(categoryDto.getAttachmentId().get(0));
            category.setAttachment(attachment);
        }
        category.setUrl(categoryDto.getUrl());
        category.setText(categoryDto.getText());
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return new Result("Given category successfully edited.", true);
    }

    public Result delete(Integer categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(categoryId, true);
        if (!optionalCategory.isPresent()) return new Result("Such category id not exist!", false);

        Category category = optionalCategory.get();
        category.setActive(false);
        categoryRepository.save(category);
        return new Result("Given category successfully deleted.", true);
    }

}
