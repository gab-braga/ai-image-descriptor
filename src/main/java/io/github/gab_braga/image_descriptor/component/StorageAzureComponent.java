package io.github.gab_braga.image_descriptor.component;

import com.azure.storage.file.share.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageAzureComponent {

    @Value("${azure-account-name}")
    private String azureAccountName;
    @Value("${azure-account-key}")
    private String azureAccountKey;

    private final int MAX_SIZE_IN_BYTES_TO_UPLOAD_FILE = 2000000;

    public void createFileShare(String shareName) throws Exception {
        ShareClient shareClient = createShareClient(shareName);
        shareClient.createIfNotExists();
    }

    public void deleteFileShare(String shareName) throws Exception {
        ShareClient shareClient = createShareClient(shareName);
        shareClient.deleteIfExists();
    }

    public void createDirectory(String shareName, String dirName) throws Exception {
        ShareDirectoryClient shareDirectoryClient = createShareDirectoryClient(shareName, dirName);
        shareDirectoryClient.createIfNotExists();
    }

    public void deleteDirectory(String shareName, String dirName) throws Exception {
        ShareDirectoryClient shareDirectoryClient = createShareDirectoryClient(shareName, dirName);
        shareDirectoryClient.deleteIfExists();
    }

    public void uploadFile(String shareName, String dirName, String fileName) throws Exception {
        ShareDirectoryClient dirClient = new ShareFileClientBuilder()
                .connectionString(createStringConnection())
                .shareName(shareName)
                .resourcePath(dirName)
                .buildDirectoryClient();
        ShareFileClient fileClient = dirClient.getFileClient(fileName);
        fileClient.create(MAX_SIZE_IN_BYTES_TO_UPLOAD_FILE);
        String pathFile = String.format("uploads/%s", fileName);
        fileClient.uploadFromFile(pathFile);
    }

    public void downloadFile(String shareName, String dirName, String fileName) throws Exception {
        ShareDirectoryClient shareDirectoryClient = createShareDirectoryClient(shareName, dirName);
        ShareFileClient fileClient = shareDirectoryClient.getFileClient(fileName);
        String pathFile = String.format("downloads/%s", fileName);
        fileClient.downloadToFile(pathFile);
    }

    public void deleteFile(String shareName, String dirName, String fileName) throws Exception {
        ShareDirectoryClient shareDirectoryClient = createShareDirectoryClient(shareName, dirName);
        ShareFileClient fileClient = shareDirectoryClient.getFileClient(fileName);
        fileClient.deleteIfExists();
    }

    private ShareClient createShareClient(String shareName) throws Exception {
        return new ShareClientBuilder()
                .connectionString(this.createStringConnection())
                .shareName(shareName)
                .buildClient();
    }

    private ShareDirectoryClient createShareDirectoryClient(String shareName, String dirName) throws Exception {
        return new ShareFileClientBuilder()
                .connectionString(this.createStringConnection())
                .shareName(shareName)
                .resourcePath(dirName)
                .buildDirectoryClient();
    }

    private String createStringConnection() {
        return String.format("DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s;EndpointSuffix=core.windows.net", azureAccountName, azureAccountKey);
    }
}
