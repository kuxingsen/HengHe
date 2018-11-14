package com.henghe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewsController{
    @RequestMapping(value = "admin/{path}")
    public String adminIndex(@PathVariable String path) {
        return "admin/"+path;
    }
    @RequestMapping("/{path}")
    public String userIndex(@PathVariable String path) {
        return path;
    }
    @RequestMapping("/")
    public String userIndex() {
        return "index";
    }
}
