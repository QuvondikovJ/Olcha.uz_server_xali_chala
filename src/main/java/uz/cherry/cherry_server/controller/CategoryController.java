package uz.cherry.cherry_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.cherry.cherry_server.payload.CategoryDto;
import uz.cherry.cherry_server.payload.Result;
import uz.cherry.cherry_server.service.CategoryService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Result> add(@Valid @RequestBody CategoryDto categoryDto) {
        Result result = categoryService.add(categoryDto);
        return ResponseEntity.status(result.isSuccess() ? 201 : 409).body(result);
    }

    @GetMapping("/grandCategories")
    public ResponseEntity<Result> getGrandCategories() {
        Result result = categoryService.getGrandCategories();
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/grandAndFatherAndChildCategories/{id}")
    public ResponseEntity<Result> getGrandAndFatherAndChildCategories(@PathVariable Integer id) {
        Result result = categoryService.getGrandAndFatherChildCategories(id);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/grandAndFatherCategories/{id}")
    public ResponseEntity<Result> getGrandAndFatherCategories(@PathVariable Integer id) {
        Result result = categoryService.getGrandAndFatherCategories(id);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/getCategoryWithRelatives/{id}")
    public ResponseEntity<Result> getCategoryWithRelatives(@PathVariable Integer id) {
        Result result = categoryService.getCategoryWithRelatives(id);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/byProductInstallmentPlan")
    public ResponseEntity<Result> getByProductInstallmentPlan() {
        Result result = categoryService.getByInstallmentPlan();
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/byProductInstallmentPlan/byCategory/{id}")
    public ResponseEntity<Result> getByProductInstallmentPlanAndCategoryId(@PathVariable Integer id) {
        Result result = categoryService.getByInstallmentPlanAndCategoryId(id);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/byProductDiscount")
    public ResponseEntity<Result> getByProductDiscount() {
        Result result = categoryService.getByProductDiscount();
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/byProductDiscount/byCategory/{id}")
    public ResponseEntity<Result> getByProductDiscountAndCategoryId(@PathVariable Integer id) {
        Result result = categoryService.getByProductDiscountAndCategoryId(id);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/byManufacturer/{manufacturerId}")
    public ResponseEntity<Result> getByManufacturer(@PathVariable Integer manufacturerId) {
        Result result = categoryService.getByManufacturerId(manufacturerId);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Integer id, @Valid @RequestBody CategoryDto categoryDto) {
        Result result = categoryService.edit(id, categoryDto);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> delete(@PathVariable Integer id) {
        Result result = categoryService.delete(id);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }


    /*  For validation */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public static Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
