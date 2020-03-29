package com.example.elk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/go")
@Controller
public class GoController {
  
  @GetMapping("/pie")
  public String goPie() {
    return "pie";
  }

  @GetMapping("/pistogram")
  public String goPistogram() {
    return "pistogram";
  }

  @GetMapping("/form")
  public String goForm() {
    return "form";
  }

}