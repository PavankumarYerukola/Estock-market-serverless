package com.estockmarket.module;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import dagger.Module;
import dagger.Provides;


public class DynamoDBModule {

	private static final String DYNAMODB_END_POINT = "https://dynamodb.us-east-1.amazonaws.com";
	private static final String AWS_REGION = "us-east-1";
	private static final String AWS_ACCESS_KEY_ID = "AKIASWWDME3DWJ5GIAVV";
	private static final String AWS_SECRET_ACCESS_KEY = "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";
	

	public static DynamoDBMapper provideDynamoDBMapper() {
		return new DynamoDBMapper(buildAmazonDynamoDB());
	}

	private static AmazonDynamoDB buildAmazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(DYNAMODB_END_POINT, AWS_REGION))
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)))
				.build();
	}

}
