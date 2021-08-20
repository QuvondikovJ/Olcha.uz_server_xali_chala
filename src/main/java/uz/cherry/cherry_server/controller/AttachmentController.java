package uz.cherry.cherry_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.cherry.cherry_server.payload.Result;
import uz.cherry.cherry_server.service.AttachmentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;


    @PostMapping
    public ResponseEntity<Result> add(@Valid MultipartHttpServletRequest request) throws IOException {
        Result result = attachmentService.add(request);
        return ResponseEntity.status(result.isSuccess() ? 201 : 409).body(result);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Result> get(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Result result = attachmentService.get(id, response);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Integer id, MultipartHttpServletRequest request) throws IOException {
        Result result = attachmentService.edit(id, request);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result> delete(@PathVariable Integer id) {
        Result result = attachmentService.delete(id);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }


}
