package com.crunchshop.messagebroker.impl.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

class AWSClient {

    private final AmazonSQS sqs;
    private final AmazonSNS sns;

    AWSClient(String awsAccessKey, String awsSecretKey, String region) {
        AmazonSQSClientBuilder sqsBuilder = AmazonSQSClientBuilder.standard();
        AmazonSNSClientBuilder snsBuilder = AmazonSNSClientBuilder.standard();

        if(awsAccessKey != null && !awsAccessKey.isEmpty()) {
            AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
            sqsBuilder
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.fromName(region));

            snsBuilder
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.fromName(region));
        } else {
            sqsBuilder
                    .withRegion(Regions.fromName(region));
            snsBuilder
                    .withRegion(Regions.fromName(region));
        }

        sqs = sqsBuilder.build();
        sns = snsBuilder.build();
    }

    AmazonSQS getSqs() {
        return sqs;
    }

    AmazonSNS getSns() {
        return sns;
    }
}
