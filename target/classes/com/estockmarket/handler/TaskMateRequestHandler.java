package com.estockmarket.handler;


import java.util.Map;
import java.util.logging.Logger;

import com.amazonaws.HttpMethod;
import com.estockmarket.exception.BusinessException;
import com.estockmarket.model.GatewayProxyRequest;
import com.estockmarket.model.GatewayResponse;
import com.estockmarket.model.RequestContext;
import com.estockmarket.module.DynamoDBModule;
import com.estockmarket.service.impl.CompanyService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class TaskMateRequestHandler extends LambdaFunctionProcessor {

	
	private static final ObjectMapper mapper;
	private static final DynamoDBModule dynamoMapper;
	private static Map<String, String> config;
	private static Logger logger= Logger.getLogger(TaskMateRequestHandler.class.getName());
	public static final String ACTION = "action";
	
	static {
		mapper = new ObjectMapper();
		dynamoMapper= new DynamoDBModule();
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		
	}

	@Override
	protected GatewayResponse processRequest(GatewayProxyRequest request, RequestContext context)
			throws BusinessException {
		Map<String, String> headers = getHeaders();
		CompanyService service= new CompanyService(mapper, dynamoMapper.provideDynamoDBMapper());
		GatewayResponse response = null;
		try {					
			logger.info("Entered into handler"+request.getBody());;
			String action =getAction(request); 			
			switch (action) {
			case "RegisterCompany":				
				response = service.register(request);
				break;
			case "SearchCompany":				
				response = service.searchCompany(request);
				break;
			case "DeleteCompany":				
				response = service.deleteCompany(request);
				break;
			case "GetAllCompanies":				
				response = service.fetchAllCompanyDetails();
				break;
			case "AddStockPrice":				
				response = service.addStockPrice(request);
				break;
			case "GetStockPrice":
				response = service.getStockPrice(request);
				break;
			default:
				response = new GatewayResponse("Action is not set in header.", headers, 400);
				break;
			}
		} catch (Exception e) {
			response = new GatewayResponse(e.getMessage(), headers, 400);
		}

		return response;
	}	
	private String getAction(GatewayProxyRequest request) throws JsonMappingException, JsonProcessingException {
		String action =null;
		if(request.getBody() != null) {
			action =mapper.readTree(request.getBody()).get(ACTION).asText();
		}else {
			String pathValue= request.getPath();
			logger.info("pathValue"+pathValue);
			if(pathValue.contains("delete")) {
				action="DeleteCompany";
			} else if(pathValue.contains("info")) {
				action="SearchCompany";
			} else if(pathValue.contains("getall")) {
				action="GetAllCompanies";
			} else if(pathValue.contains("stock")) {
				action="GetStockPrice";
			}
		}
		return action;
	}
}
