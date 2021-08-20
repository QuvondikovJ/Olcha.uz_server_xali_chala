package uz.cherry.cherry_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.cherry.cherry_server.service.CharDepartmentService;

@RestController
@RequestMapping("/api/characteristic-department")
public class CharDepartmentController {


    @Autowired
    CharDepartmentService charDepartmentService;

}
