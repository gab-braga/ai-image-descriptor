package io.github.gab_braga.image_cloud.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;

import io.github.gab_braga.image_cloud.component.BlobStorageAzureComponent;
import io.github.gab_braga.image_cloud.component.ShareStorageAzureComponent;
import io.github.gab_braga.image_cloud.model.Image;

@Service
public class AppService {

  private final String CONTAINER_NAME = "files";
  private final String SHARE_NAME = "files";
  private final String SHARE_DIRECTORY_NAME = "images";
  
  @Autowired
  private ShareStorageAzureComponent shareStorageAzure;

  @Autowired
  private BlobStorageAzureComponent blobStorageAzure;

  public String uploadFileWithBlobStorage(MultipartFile file) throws Exception {
    String fileName = this.saveFile(file);
    this.blobStorageAzure.createBlobContainer(CONTAINER_NAME);
    BlobClient blobClient = this.blobStorageAzure.uploadFile(CONTAINER_NAME, fileName);
    return blobClient.getBlobUrl();
  }

  public void uploadFileWithShareStorage(MultipartFile file) throws Exception {
    String fileName = this.saveFile(file);
    this.shareStorageAzure.createFileShare(SHARE_NAME);
    this.shareStorageAzure.createDirectory(SHARE_NAME, SHARE_DIRECTORY_NAME);
    this.shareStorageAzure.uploadFile(SHARE_NAME, SHARE_DIRECTORY_NAME, fileName);
  }

  public List<Image> findImages() throws Exception {
    return this.blobStorageAzure.findImages(CONTAINER_NAME);
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
}
