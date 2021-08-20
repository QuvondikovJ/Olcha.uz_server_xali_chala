package uz.cherry.cherry_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.cherry.cherry_server.payload.ProductDto;
import uz.cherry.cherry_server.payload.Result;
import uz.cherry.cherry_server.service.ProductService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    public ResponseEntity<Result> add(@Valid @RequestBody ProductDto productDto) {
        Result result = productService.add(productDto);
        return ResponseEntity.status(result.isSuccess() ? 201 : 409).body(result);
    }

    @GetMapping("/oneProduct/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        Result result = productService.getById(id);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/byCategory/{id}")
    public ResponseEntity<Result> getByCategoryId(@PathVariable Integer id, @RequestParam int page, @RequestParam int sort) {
        Result result = productService.getByCategoryId(id, page, sort);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/byDiscount")
    public ResponseEntity<Result> getByDiscount(@RequestParam int page, @RequestParam int sort) {
        Result result = productService.getByDiscount(page, sort);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/byInstallmentPlan")
    public ResponseEntity<Result> getByInstallmentPlan(@RequestParam int page, @RequestParam int sort) {
        Result result = productService.getByInstallmentPlan(page, sort);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }


    @GetMapping("/byDiscount/byCategory/{id}")
    public ResponseEntity<Result> getByDiscountAndCategoryId(@PathVariable Integer id, @RequestParam int page, @RequestParam int sort) {
        Result result = productService.getByDiscountAndCategoryId(id, page, sort);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/byInstallmentPlan/byCategory/{id}")
    public ResponseEntity<Result> getByInstallmentPlanAndCategory(@PathVariable Integer id, @RequestParam int page, @RequestParam int sort) {
        Result result = productService.getByInstallmentPlanAndCategoryId(id, page, sort);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/byManufacturer/byCategory/{categoryId}")
    public ResponseEntity<Result> getByCategoryAndManufacturer(@PathVariable Integer categoryId, @RequestParam int manufacturerId, @RequestParam int page) {
        Result result = productService.getByCategoryIdAndManufacturerId(categoryId, manufacturerId, page);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Integer id, @Valid @RequestBody ProductDto productDto) {
        Result result = productService.edit(id, productDto);
        return ResponseEntity.status(result.isSuccess() ? 202 : 409).body(result);
    }

    @PutMapping("/editIsFastDelivery/{id}")
public ResponseEntity<Result> editIsFastDelivery(@PathVariable Integer id){
        Result result = productService.editIsFastDelivery(id);
        return ResponseEntity.status(result.isSuccess() ? 202 : 409).body(result);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Result> delete(@PathVariable Integer id) {
        Result result = productService.delete(id);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }


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
