package io.github.gab_braga.image_cloud.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;

@Component
public class BlobStorageAzureComponent {

    @Autowired
    private StorageAccount account;

    public void createBlobContainer(String containerName) throws Exception {
        BlobContainerClient blobContainerClient = this.account.createBlobContainerClient(containerName);
        blobContainerClient.createIfNotExists();
    }

    public String uploadFile(String containerName, String fileName) throws Exception {
        BlobContainerClient blobContainer = this.account.createBlobContainerClient(containerName);
        BlobClient blobClient = blobContainer.getBlobClient(fileName);
        String pathFile = String.format("uploads/%s", fileName);
        blobClient.uploadFromFile(pathFile);
        return blobClient.getBlobUrl();
    }
}
