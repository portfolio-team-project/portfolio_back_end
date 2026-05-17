package com.api.domain.base.ds.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ds")
public class DsController {
    @GetMapping("/hello")
    public String hello() {
        return "hello spring";
    }
}
