package io.github.gab_braga.image_cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.gab_braga.image_cloud.service.AppService;

@Controller
@RequestMapping("/")
public class AppController {

  @Autowired
  private AppService service;

  @GetMapping
  public ModelAndView home() {
    return new ModelAndView("home");
  }

  @PostMapping
  public String submit(RedirectAttributes attributes, @RequestParam("image") MultipartFile file) {
    try {
      String url = this.service.uploadImage(file);
      attributes.addAttribute("url", url);
      attributes.addAttribute("caption", "");
      return "redirect:/captions";
    } catch(Exception e) {
      System.err.println(e.getMessage());
      return "redirect:/";
    }
  }
  
  @GetMapping("/captions")
  public ModelAndView captions() {
    return new ModelAndView("captions");
  }
}
