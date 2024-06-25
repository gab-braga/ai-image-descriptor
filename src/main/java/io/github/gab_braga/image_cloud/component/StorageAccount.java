package io.github.gab_braga.image_cloud.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.file.share.ShareClient;
import com.azure.storage.file.share.ShareClientBuilder;
import com.azure.storage.file.share.ShareDirectoryClient;
import com.azure.storage.file.share.ShareFileClientBuilder;

@Component
public class StorageAccount {

    @Value("${azure-account-name}")
    private String azureAccountName;
    @Value("${azure-account-key}")
    private String azureAccountKey;

    public ShareClient createShareClient(String shareName) throws Exception {
        return new ShareClientBuilder()
                .connectionString(this.createStringConnection())
                .shareName(shareName)
                .buildClient();
    }

    public ShareDirectoryClient createShareDirectoryClient(String shareName, String dirName) throws Exception {
        return new ShareFileClientBuilder()
                .connectionString(this.createStringConnection())
                .shareName(shareName)
                .resourcePath(dirName)
                .buildDirectoryClient();
    }

    public BlobContainerClient createBlobContainerClient(String containerName) throws Exception {
        return new BlobContainerClientBuilder()
                .connectionString(this.createStringConnection())
                .containerName(containerName)
                .buildClient();
    }

    private String createStringConnection() {
        return String.format("DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s;EndpointSuffix=core.windows.net", azureAccountName, azureAccountKey);
    }
}
