package com.javaaws.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;


    @Autowired
    private AmazonS3 s3Client;

    public String uploadFileToAwsS3(MultipartFile multipartFile) throws IOException {
        String fileName = getFileName(multipartFile);
        File aFile = convertMultiPartFileToFile(multipartFile);
        s3Client.putObject(new PutObjectRequest(bucketName,fileName, aFile));
        aFile.delete();
        return fileName+" uploaded Successfully to AWS S3";
    }

    public byte[] downloadFileFromAwsS3(String fileName) throws IOException {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName,fileName));
        try (S3ObjectInputStream objectInputStream = s3Object.getObjectContent()) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = objectInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            // Handle the exception
            throw new IOException("Failed to convert S3Object to byte array", e);
        }
    }

    public String deleteFileFromAwsS3(String fileName){
        s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
        return fileName+" deleted Successfully to AWS S3";
    }

    public List<String> getListObject(){
        List<String> result = new ArrayList<>();
        ListObjectsRequest listObjects =  new ListObjectsRequest( bucketName, null, null, null, Integer.valueOf(20));
        ObjectListing res = s3Client.listObjects(listObjects);
        List<S3ObjectSummary>  s3ObjectSummaryList = res.getObjectSummaries();
        for(S3ObjectSummary anObjectSummary : s3ObjectSummaryList){
            result.add(anObjectSummary.getKey());
        }
        return result;
    }


    public static File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
        // Create a temporary file
        File file = File.createTempFile("temp", null);

        // Convert MultipartFile to File
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            log.error("Error Converting multipartFile to File");
            throw new IOException("Failed to convert multipart file to file", e);
        }

        return file;
    }

    private String getFileName(MultipartFile file) {
        return  System.currentTimeMillis()+"-"+file.getOriginalFilename();
    }
}
