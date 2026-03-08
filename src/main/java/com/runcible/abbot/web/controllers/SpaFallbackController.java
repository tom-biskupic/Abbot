package com.runcible.abbot.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaFallbackController {

    @GetMapping(value = { "/{path:[^\\.]*}", "/{path1:[^\\.]*}/{path2:[^\\.]*}" })
    public String forwardSpa() {
        return "forward:/index.html";
    }
}
