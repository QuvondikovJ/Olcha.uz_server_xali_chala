package uz.cherry.cherry_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.cherry.cherry_server.entity.Category;
import uz.cherry.cherry_server.entity.Manufacturer;
import uz.cherry.cherry_server.payload.Result;
import uz.cherry.cherry_server.repository.CategoryRepository;
import uz.cherry.cherry_server.repository.ManufacturerRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ManufacturerService {

    @Autowired
    ManufacturerRepository manufacturerRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public Result add(Manufacturer manufacturer) {
        boolean existsByNameAndActive = manufacturerRepository.existsByNameAndActive(manufacturer.getName(), true);
        if (existsByNameAndActive) return new Result("Such manufacturer already added!", false);
        manufacturerRepository.save(manufacturer);
        return new Result("New manufacturer successfully saved.", true);
    }

    public Result get() {
        List<Manufacturer> manufacturers = manufacturerRepository.getByActive(true);
        if (manufacturers.size() == 0) return new Result("No manufacturer has been added yet!", false);
        return new Result(manufacturers, true);
    }

    public Result getByCategoryId(Integer categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndActive(categoryId, true);
        if (!optionalCategory.isPresent()) return new Result("Such category id not exist!", false);

        if (optionalCategory.get().getParentCategory() == null)
            return new Result("It is not possible to add products to this category, so there will be no manufacturers in this category!", false);

        boolean existsChildCategoryByParentCategory = categoryRepository.existsByParentCategoryIdAndActive(categoryId, true);

        if (existsChildCategoryByParentCategory) {
            Set<Manufacturer> getByParentCategoryId = manufacturerRepository.getByParentCategoryId(categoryId, true);
            if (getByParentCategoryId.size() == 0)
                return new Result("There is currently no manufacturer for this category!", false);
            return new Result(getByParentCategoryId, true);
        } else {
            Set<Manufacturer> getByCategoryId = manufacturerRepository.getByCategoryId(categoryId, true);
            if (getByCategoryId.size() == 0)
                return new Result("There is currently no manufacturer for this category!", false);
            return new Result(getByCategoryId, true);
        }
    }


    public Result edit(Integer id, Manufacturer newManufacturer) {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findByIdAndActive(id, true);
        if (!optionalManufacturer.isPresent()) return new Result("Such manufacturer id not exist!", false);

        boolean existsByNameAndActive = manufacturerRepository.existsByNameAndActive(newManufacturer.getName(), true);
        if (existsByNameAndActive) return new Result("Such manufacturer already exist!", false);
        Manufacturer manufacturer = optionalManufacturer.get();
        manufacturer.setName(newManufacturer.getName());
        manufacturerRepository.save(manufacturer);
        return new Result("Given manufacturer successfully edited.", true);
    }

    public Result delete(Integer id) {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findByIdAndActive(id, true);
        if (!optionalManufacturer.isPresent()) return new Result("Such manufacturer id not exist!", false);

        Manufacturer manufacturer = optionalManufacturer.get();
        manufacturer.setActive(false);
        manufacturerRepository.save(manufacturer);
        return new Result("Given manufacturer successfully deleted.", true);
    }


}
