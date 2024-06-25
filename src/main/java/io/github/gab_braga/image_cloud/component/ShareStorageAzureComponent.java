package io.github.gab_braga.image_cloud.component;

import com.azure.storage.file.share.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShareStorageAzureComponent {

    @Autowired
    private StorageAccount account;

    private final int MAX_SIZE_IN_BYTES_TO_UPLOAD_FILE = 2000000;

    public void createFileShare(String shareName) throws Exception {
        ShareClient shareClient = this.account.createShareClient(shareName);
        shareClient.createIfNotExists();
    }

    public void deleteFileShare(String shareName) throws Exception {
        ShareClient shareClient = this.account.createShareClient(shareName);
        shareClient.deleteIfExists();
    }

    public void createDirectory(String shareName, String dirName) throws Exception {
        ShareDirectoryClient shareDirectoryClient = this.account.createShareDirectoryClient(shareName, dirName);
        shareDirectoryClient.createIfNotExists();
    }

    public void deleteDirectory(String shareName, String dirName) throws Exception {
        ShareDirectoryClient shareDirectoryClient = this.account.createShareDirectoryClient(shareName, dirName);
        shareDirectoryClient.deleteIfExists();
    }

    public void uploadFile(String shareName, String dirName, String fileName) throws Exception {
        ShareDirectoryClient dirClient = this.account.createShareDirectoryClient(shareName, dirName);
        ShareFileClient fileClient = dirClient.getFileClient(fileName);
        fileClient.create(MAX_SIZE_IN_BYTES_TO_UPLOAD_FILE);
        String pathFile = String.format("uploads/%s", fileName);
        fileClient.uploadFromFile(pathFile);
    }

    public void downloadFile(String shareName, String dirName, String fileName) throws Exception {
        ShareDirectoryClient shareDirectoryClient = this.account.createShareDirectoryClient(shareName, dirName);
        ShareFileClient fileClient = shareDirectoryClient.getFileClient(fileName);
        String pathFile = String.format("downloads/%s", fileName);
        fileClient.downloadToFile(pathFile);
    }

    public void deleteFile(String shareName, String dirName, String fileName) throws Exception {
        ShareDirectoryClient shareDirectoryClient = this.account.createShareDirectoryClient(shareName, dirName);
        ShareFileClient fileClient = shareDirectoryClient.getFileClient(fileName);
        fileClient.deleteIfExists();
    }
}
