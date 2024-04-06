package com.javaaws.s3.config;

import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.regions.Region;

public class SecretConfig {

//    public static void getSecret() {
//
//        String secretName = "aws/secret";
//        Region region = Region.of("us-east-1");
//
//        // Create a Secrets Manager client
//        SecretsManagerClient client = SecretsManagerClient.builder()
//                .region(region)
//                .build();
//
//        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
//                .secretId(secretName)
//                .build();
//
//        GetSecretValueResponse getSecretValueResponse;
//
//        try {
//            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
//        } catch (Exception e) {
//            // For a list of exceptions thrown, see
//            // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
//            throw e;
//        }
//
//        String secret = getSecretValueResponse.secretString();
//
//        // Your code goes here.
//    }
}
