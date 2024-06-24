package io.github.gab_braga.image_descriptor.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.gab_braga.image_descriptor.component.azure.StorageAccount;

@Service
public class AppService {

  private final String SHARE_NAME = "files";
  private final String DIRECTORY_NAME = "images";
  
  @Autowired
  private StorageAccount storage;

  public String uploadImage(MultipartFile file) throws Exception {
    String fileName = this.saveFile(file);
    return this.uploadFile(fileName);
  }

  private String saveFile(MultipartFile file) throws IOException {
    String currentDirectory = System.getProperty("user.dir");
    String pathDirectory =  currentDirectory + "/uploads";
    String fileName = file.getOriginalFilename();
    String fileType = extractFileType(fileName);
    String fileNameUnique = this.uuid() + fileType;
    Path fileNameAndPath = Paths.get(pathDirectory, fileNameUnique);
    Files.write(fileNameAndPath, file.getBytes());
    return fileNameUnique;
  }

  private String extractFileType(String fileName) {
    int index = fileName.lastIndexOf(".");
    return fileName.substring(index);
  }

  private String uuid() {
    return UUID.randomUUID().toString();
  }

  private String uploadFile(String fileName) throws Exception {
    this.storage.createFileShare(SHARE_NAME);
    this.storage.createDirectory(SHARE_NAME, DIRECTORY_NAME);
    this.storage.uploadFile(SHARE_NAME, DIRECTORY_NAME, fileName);
    return "";
  }
}
