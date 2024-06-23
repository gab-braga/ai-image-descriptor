package io.github.gab_braga.image_descriptor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import io.github.gab_braga.image_descriptor.component.azure.StorageAccount;

@Controller
@RequestMapping("/")
public class HomeController {

  @Autowired
  private StorageAccount storage;

  @GetMapping
  public ModelAndView home() {
    return new ModelAndView("home");
  }

  @PostMapping
  public String submit() {
    try {
      storage.createFileShare("files");
      storage.createDirectory("files", "images");
      storage.uploadFile("files", "images", "");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return "redirect:/";
  }
}
