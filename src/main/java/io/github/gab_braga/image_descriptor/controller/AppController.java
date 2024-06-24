package io.github.gab_braga.image_descriptor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class AppController {

  @GetMapping
  public ModelAndView home() {
    return new ModelAndView("home");
  }
  
  @GetMapping("/captions")
  public ModelAndView captions() {
    return new ModelAndView("captions");
  }

  @PostMapping
  public String submit(RedirectAttributes attributes, @RequestParam("image") MultipartFile file) {
    System.out.println(file.getOriginalFilename());
    attributes.addAttribute("url", "");
    attributes.addAttribute("caption", "");
    return "redirect:/captions";
  }
}
