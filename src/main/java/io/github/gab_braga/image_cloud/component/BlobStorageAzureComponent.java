package io.github.gab_braga.image_cloud.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobItem;

import io.github.gab_braga.image_cloud.model.Image;


@Component
public class BlobStorageAzureComponent {

    @Autowired
    private StorageAccount account;

    public void createBlobContainer(String containerName) throws Exception {
        BlobContainerClient blobContainerClient = this.account.createBlobContainerClient(containerName);
        blobContainerClient.createIfNotExists();
    }

    public BlobClient uploadFile(String containerName, String fileName) throws Exception {
        BlobContainerClient blobContainer = this.account.createBlobContainerClient(containerName);
        BlobClient blobClient = blobContainer.getBlobClient(fileName);
        String pathFile = String.format("uploads/%s", fileName);
        blobClient.uploadFromFile(pathFile);
        return blobClient;
    }

    public List<Image> findImages(String containerName) throws Exception {
        BlobContainerClient blobContainer = this.account.createBlobContainerClient(containerName);
        List<Image> images = new ArrayList<Image>();
        PagedIterable<BlobItem> blobs = blobContainer.listBlobs();
        blobs.forEach(blob -> {
            String name = blob.getName();
            String url = blobContainer.getBlobClient(name).getBlobUrl();
            images.add(new Image(name, url));
        });
        return images;
    }
}
