package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // TODO este controller nao tem sentido existir

    @RequestMapping("index")
    public String index() {
    	System.out.print("==========Hello world==============");
        return "It is working!";
    }

}
