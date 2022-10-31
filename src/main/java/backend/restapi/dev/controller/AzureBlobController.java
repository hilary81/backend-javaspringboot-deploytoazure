package backend.restapi.dev.controller;

import backend.restapi.dev.service.AzureBlobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 86000)
@RestController
public class AzureBlobController {

    @Autowired
    private AzureBlobService azureBlobService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {

        String fileName = azureBlobService.upload(file);
        return ResponseEntity.ok(fileName);
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> getAllBlobs(){

        List<String> items = azureBlobService.listBlobs();

        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Boolean> delele(@PathVariable("name") String name) {
        azureBlobService.deleteBlob(name);

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/download/{name}")
    public ResponseEntity<Resource> getFile(@PathVariable("name") String name) {

        ByteArrayResource resource = new ByteArrayResource(azureBlobService.getFile(name));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +name + "\"");

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).headers(headers).body(resource);

    }
}
