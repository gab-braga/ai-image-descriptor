package io.github.gab_braga.image_descriptor.component.azure;

import com.azure.storage.file.share.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageAccount {

    @Value("azure-account-name")
    private String azureAccountName;
    @Value("azure-account-key")
    private String azureAccountKey;

    public final String connectStr =
            "DefaultEndpointsProtocol=https;" +
                    String.format("AccountName=%s;", azureAccountName) +
                    String.format("AccountKey=%s", azureAccountKey);

    private ShareClient createShareClientConnection
            (String connectStr, String shareName) throws Exception {
        return new ShareClientBuilder()
                .connectionString(connectStr)
                .shareName(shareName)
                .buildClient();
    }

    private ShareDirectoryClient createShareDirectoryClientConnection
            (String connectStr, String shareName, String dirName) throws Exception {
        return new ShareFileClientBuilder()
                .connectionString(connectStr)
                .shareName(shareName)
                .resourcePath(dirName)
                .buildDirectoryClient();
    }

    public void createFileShare(String connectStr, String shareName) throws Exception {
        ShareClient shareClient = createShareClientConnection(connectStr, shareName);
        shareClient.create();
    }

    public void deleteFileShare(String connectStr, String shareName) throws Exception {
        ShareClient shareClient = createShareClientConnection(connectStr, shareName);
        shareClient.delete();
    }

    public void createDirectory(String connectStr, String shareName, String dirName) throws Exception {
        ShareDirectoryClient shareDirectoryClient =
                createShareDirectoryClientConnection(connectStr, shareName, dirName);
        shareDirectoryClient.create();
    }

    public void deleteDirectory(String connectStr, String shareName, String dirName) throws Exception {
        ShareDirectoryClient shareDirectoryClient =
                createShareDirectoryClientConnection(connectStr, shareName, dirName);
        shareDirectoryClient.delete();
    }

    public void uploadFile(String connectStr, String shareName, String dirName, String fileName) throws Exception {
        ShareDirectoryClient shareDirectoryClient =
                createShareDirectoryClientConnection(connectStr, shareName, dirName);
        ShareFileClient fileClient = shareDirectoryClient.getFileClient(fileName);
        fileClient.create(1024);
        fileClient.uploadFromFile(fileName);
    }

    public void downloadFile(String connectStr, String shareName, String dirName, String fileName) throws Exception {
        ShareDirectoryClient shareDirectoryClient =
                createShareDirectoryClientConnection(connectStr, shareName, dirName);
        ShareFileClient fileClient = shareDirectoryClient.getFileClient(fileName);
        fileClient.downloadToFile(fileName);
    }

    public void deleteFile(String connectStr, String shareName, String dirName, String fileName) throws Exception {
        ShareDirectoryClient shareDirectoryClient =
                createShareDirectoryClientConnection(connectStr, shareName, dirName);
        ShareFileClient fileClient = shareDirectoryClient.getFileClient(fileName);
        fileClient.delete();
    }
}
