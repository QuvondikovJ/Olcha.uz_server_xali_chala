package uz.cherry.cherry_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.cherry.cherry_server.entity.ColorAndPhotoForProduct;
import uz.cherry.cherry_server.payload.ColorAndPhotoForProductDto;
import uz.cherry.cherry_server.payload.Result;
import uz.cherry.cherry_server.service.ColorAndPhotoForProductService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/color-and-photo")
public class ColorAndPhotoForProductController {


    @Autowired
    ColorAndPhotoForProductService colorAndPhotoForProductService;

    @PostMapping
    public ResponseEntity<Result> add(@Valid @RequestBody ColorAndPhotoForProductDto colorAndPhotoForProductDto) {
        Result result = colorAndPhotoForProductService.add(colorAndPhotoForProductDto);
        return ResponseEntity.status(result.isSuccess() ? 201 : 409).body(result);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Result> getByProductId(@PathVariable Integer productId) {
        Result result = colorAndPhotoForProductService.getByProductId(productId);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }


    @PutMapping("/{productId}")
    public ResponseEntity<Result> edit(@PathVariable Integer productId, @Valid @RequestBody ColorAndPhotoForProductDto colorAndPhotoForProductDto) {
        Result result = colorAndPhotoForProductService.edit(productId, colorAndPhotoForProductDto);
        return ResponseEntity.status(result.isSuccess() ? 202 : 409).body(result);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Result> delete(@PathVariable Integer productId) {
        Result result = colorAndPhotoForProductService.delete(productId);
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
