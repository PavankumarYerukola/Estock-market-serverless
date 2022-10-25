package com.estockmarket.domain;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@DynamoDBTable(tableName = "EstockMarket")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {		
	
	@DynamoDBHashKey(attributeName = "companyCode")
	private String companyCode;

	@DynamoDBAttribute
	private String companyName;

	@DynamoDBAttribute
	private String companyCEO;

	@DynamoDBAttribute
	private String companyTurnover;

	@DynamoDBAttribute
	private String website;

	
	@DynamoDBAttribute
	private String createdBy;

	@DynamoDBAttribute
	private String stockExng;
	
	@DynamoDBAttribute
	private String stockDetails;
	
	@DynamoDBAttribute
	private String currentStockPrice;

}
