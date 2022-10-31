package backend.restapi.dev.service;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

@Component
public class AzureBlobService {

    @Autowired
    BlobServiceClient blobServiceClient;

    @Autowired
    BlobContainerClient blobContainerClient;

    public String upload(@NotNull MultipartFile multipartFile) throws IOException{

        BlobClient blob = blobContainerClient.getBlobClient(multipartFile.getOriginalFilename());
        blob.upload(multipartFile.getInputStream(), multipartFile.getSize(), true);

        return multipartFile.getOriginalFilename();
    }

    public byte[] getFile(String fileName){

        BlobClient blob = blobContainerClient.getBlobClient(fileName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blob.downloadStream(outputStream);
        final byte[] bytes = outputStream.toByteArray();
        return bytes;
    }

    public List<String> listBlobs(){

        PagedIterable<BlobItem> items = blobContainerClient.listBlobs();

        List<String> names = new ArrayList<>();
        for (BlobItem item : items) {
            names.add(item.getName());
        }
        return names;
    }
    public Boolean deleteBlob(String blobName){

        BlobClient blob = blobContainerClient.getBlobClient(blobName);
        blob.delete();
        return true;
    }
}
