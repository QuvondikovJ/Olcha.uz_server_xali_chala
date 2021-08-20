package uz.cherry.cherry_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.cherry.cherry_server.entity.Manufacturer;
import uz.cherry.cherry_server.payload.Result;
import uz.cherry.cherry_server.service.ManufacturerService;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/manufacturer")
public class ManufacturerController {

    @Autowired
    ManufacturerService manufacturerService;


    @PostMapping
    public ResponseEntity<Result> add(@Valid @RequestBody Manufacturer manufacturer) {
        Result result = manufacturerService.add(manufacturer);
        return ResponseEntity.status(result.isSuccess() ? 201 : 409).body(result);
    }

    @GetMapping("/all")
    public ResponseEntity<Result> getAll() {
        Result result = manufacturerService.get();
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    /* Shu metodni category va productni CRUD qilib bo'lgandan keyin tekshirib ko'rish kerak */
    @GetMapping("/byCategory/{categoryId}")
    public ResponseEntity<Result> getByCategoryId(@PathVariable Integer categoryId) {
        Result result = manufacturerService.getByCategoryId(categoryId);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Integer id, @Valid @RequestBody Manufacturer manufacturer) {
        Result result = manufacturerService.edit(id, manufacturer);
        return ResponseEntity.status(result.isSuccess() ? 202 : 409).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> delete(@PathVariable Integer id) {
        Result result = manufacturerService.delete(id);
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
