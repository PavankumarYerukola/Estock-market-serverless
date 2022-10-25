package com.estockmarket.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.estockmarket.domain.Company;
import com.estockmarket.handler.TaskMateRequestHandler;
import com.estockmarket.model.GatewayProxyRequest;
import com.estockmarket.model.GatewayResponse;
import com.estockmarket.model.RequestContext;
import com.estockmarket.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Multiset.Entry;


public class CompanyService{

	private ObjectMapper mapper;
	private DynamoDBMapper dbMapper;
	private static final String REGISTER = "register";
	private static final String LOGIN = "login";
	private static final String REMOVE = "remove";
	private RequestContext ctx = null;
	private static Logger logger= Logger.getLogger(TaskMateRequestHandler.class.getName());

	public CompanyService(ObjectMapper mapper, DynamoDBMapper dbMapper) {
		this.dbMapper = dbMapper;
		this.mapper = mapper;
	}

	public GatewayResponse register(GatewayProxyRequest request) {
		String body;	
		try {	
			Company user = mapper.readValue(request.getBody(), Company.class);
			if (null == fetchCompanyByCompanyCode(user.getCompanyCode())) {				
				dbMapper.save(user);				
				body = "Registered Sucessfully";
				logger.info("Compnay Registered");
				return new GatewayResponse(body,CommonUtils.getHeaders(), 200);
			} else {
				body = String.format("%s already Registered.", user.getCompanyName());
				logger.info("Compnay Existed");
				return new GatewayResponse(body,CommonUtils.getHeaders(), 400);
			}
		} catch (Exception e) {		
			logger.info("Error in service"+e.getMessage());	
			body=e.getMessage();
			return new GatewayResponse(body,CommonUtils.getHeaders(), 500);
		}
			
		
	}	

	public Company fetchCompanyByCompanyCode(String companyCode) {
		Company user = null;
		try {
		logger.info("fetching company details");
		logger.info("primary key"+companyCode);	
		
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(companyCode));		
		
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("companyCode = :val1")
				.withExpressionAttributeValues(eav);

		List<Company> users = dbMapper.scan(Company.class, scanExpression);
		if (users.size() > 0) {
			user = users.get(0);			
		}
		return user;
		} catch(Exception e) {		
			logger.info(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	
	}
	public GatewayResponse fetchAllCompanyDetails() {
		Company user = null;
		try {
		logger.info("fetching All company details");		
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		List<Company> companies = dbMapper.scan(Company.class, scanExpression);
		if (companies.size() > 0) {
			return new GatewayResponse(mapper.writeValueAsString(companies),CommonUtils.getHeaders(), 200);		
		}
		else {
			return new GatewayResponse("COMPANY DETAILS NOT FOUND",CommonUtils.getHeaders(), 404);
		}		
		} catch(Exception e) {		
			logger.info(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	
	}
	
	public GatewayResponse searchCompany(GatewayProxyRequest request) throws JsonProcessingException {
		Map<String,String> pathParams=request.getPathParameters();
		logger.info("Path params"+mapper.writeValueAsString(pathParams));
		String companyCode=pathParams.get("companycode");				
		Company compnay= fetchCompanyByCompanyCode(companyCode);
		if(compnay!=null) {
			return new GatewayResponse(mapper.writeValueAsString(compnay),CommonUtils.getHeaders(), 200);
		}else {
			return new GatewayResponse("COMPANY DETAILS NOT FOUND",CommonUtils.getHeaders(), 404);
		}
	}
	public GatewayResponse deleteCompany(GatewayProxyRequest request) throws JsonProcessingException {
		try {
			logger.info("Deleting the company");	
			Map<String,String> pathParams=request.getPathParameters();
			logger.info("Path params"+mapper.writeValueAsString(pathParams));
			String companyCode=pathParams.get("companycode");
			Company company=fetchCompanyByCompanyCode(companyCode);
			dbMapper.delete(company);
			return new GatewayResponse("Deleted Successfully",CommonUtils.getHeaders(), 200);
		}catch(Exception e) {
			logger.info(e.getMessage());
			return new GatewayResponse("Unable to delete at this moment",CommonUtils.getHeaders(), 400);
		}
	}
	
	public GatewayResponse addStockPrice(GatewayProxyRequest request) throws JsonProcessingException {
		String body=null;
		Map<String,String> pathParams=request.getPathParameters();
		logger.info("Path params"+mapper.writeValueAsString(pathParams));
		String companyCode=pathParams.get("companycode");		
		Map<String,String> stockMap=mapper.readValue(request.getBody(), Map.class);
		String stockPrice=stockMap.get("currentStockPrice");
		Company company=fetchCompanyByCompanyCode(companyCode);
		
		try {				
			if (null!=company) {
				company.setCurrentStockPrice(stockPrice);
				Map<String,String> stockDetails= new HashMap();
				if(company.getStockDetails()== null) {				
					stockDetails.put(Instant.now().toString(),stockPrice);
					
				} else {
					stockDetails=mapper.readValue(company.getStockDetails(), Map.class);
					stockDetails.put(Instant.now().toString(), stockPrice);
				}
				company.setStockDetails(mapper.writeValueAsString(stockDetails));
				dbMapper.save(company);				
				body = "Stock Details Added";
				logger.info("Stock Details Added");
				return new GatewayResponse(body,CommonUtils.getHeaders(), 200);
			} else {
				body =companyCode+"is not registered";
				logger.info("Compnay is not there");
				return new GatewayResponse(body,CommonUtils.getHeaders(), 400);
			}
		} catch (Exception e) {		
			logger.info("Error in service"+e.getMessage());	
			body=e.getMessage();
			return new GatewayResponse(body,CommonUtils.getHeaders(), 500);
		}
			
		
	}
	public GatewayResponse getStockPrice(GatewayProxyRequest request) throws JsonProcessingException {		
		try {
			Map<String,String> pathParams=request.getPathParameters();
			logger.info("Path params"+mapper.writeValueAsString(pathParams));
			String companyCode=pathParams.get("companycode");	
			Company company=fetchCompanyByCompanyCode(companyCode);
			if (null!=company) {
			Map<String,String> stockDetails= new HashMap();						
			
			Map<String,Object> companyDetails =new HashMap<>();
			companyDetails.put("companyCode", company.getCompanyCode());
			companyDetails.put("companyName", company.getCompanyName());
			companyDetails.put("companyTurnOver", company.getCompanyTurnover());
			if(company.getStockDetails()!= null) {
				logger.info("Stock Details found");				
				stockDetails=mapper.readValue(company.getStockDetails(), Map.class);
				companyDetails.put("stockDetails", stockDetails);
				List<Double> stockValueList =new ArrayList<>();
				for(Map.Entry<String, String> stock:stockDetails.entrySet()) {
					Double stockValue=Double.parseDouble(stock.getValue());
					stockValueList.add(stockValue);
				}
				stockDetails.put("minStockPrice", Collections.min(stockValueList).toString());
				stockDetails.put("maxStockPrice", Collections.max(stockValueList).toString());
			
			} else {
				logger.info("Stock Details not found");
				companyDetails.put("stockDetails", "Stock details are not found for "+company.getCompanyName());
			}
			
			return new GatewayResponse(mapper.writeValueAsString(companyDetails),CommonUtils.getHeaders(), 200);			
			}else {
			return new GatewayResponse("COMPANY DETAILS NOT FOUND",CommonUtils.getHeaders(), 404);
		}
	} catch (Exception e) {		
		logger.info("Error in service"+e.getMessage());	
		return new GatewayResponse(e.getMessage(),CommonUtils.getHeaders(), 500);
	}
	}
}
