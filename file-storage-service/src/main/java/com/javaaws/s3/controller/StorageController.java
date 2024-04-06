package com.javaaws.s3.controller;


import com.javaaws.s3.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam (value = "file")MultipartFile file) throws IOException {
        String result = storageService.uploadFileToAwsS3(file);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) throws IOException {
        byte[] data = storageService.downloadFileFromAwsS3(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                    .ok()
                    .contentLength(data.length)
                    .header("Content-type","application/octet-stream")
                    .header("Content-disposition","attachment;filename=\""+fileName+ "\"")
                    .body(resource);
    }

    @DeleteMapping ("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName){
        String result = storageService.deleteFileFromAwsS3(fileName);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


    @GetMapping("/listObject")
    public ResponseEntity<List<String>> getListObjects(){
        List<String> result = storageService.getListObject();
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
