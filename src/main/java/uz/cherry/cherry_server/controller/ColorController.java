package uz.cherry.cherry_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.cherry.cherry_server.entity.Color;
import uz.cherry.cherry_server.payload.Result;
import uz.cherry.cherry_server.service.ColorService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/color")
public class ColorController {

    @Autowired
    ColorService colorService;

    @PostMapping
    public ResponseEntity<Result> add(@Valid @RequestBody Color color) {
        Result result = colorService.add(color);
        return ResponseEntity.status(result.isSuccess() ? 201 : 409).body(result);
    }


    @GetMapping("/all")
    public ResponseEntity<Result> getAll() {
        Result result = colorService.get();
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        Result result = colorService.getById(id);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Integer id, @Valid @RequestBody Color color) {
        Result result = colorService.edit(id, color);
        return ResponseEntity.status(result.isSuccess() ? 202 : 409).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> delete(@PathVariable Integer id) {
        Result result = colorService.delete(id);
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
