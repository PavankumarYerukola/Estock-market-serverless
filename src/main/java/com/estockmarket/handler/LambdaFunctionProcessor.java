package com.estockmarket.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.estockmarket.exception.BusinessException;
import com.estockmarket.model.GatewayProxyRequest;
import com.estockmarket.model.GatewayResponse;
import com.estockmarket.model.RequestContext;
import com.estockmarket.service.impl.CompanyService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;





public abstract class LambdaFunctionProcessor implements RequestHandler<GatewayProxyRequest, GatewayResponse> {

	protected static Map<String, Map<String, String>> configMap;
	protected static final ObjectMapper mapper;
	private static Logger logger= Logger.getLogger(TaskMateRequestHandler.class.getName());


	static {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);

	}

	protected abstract GatewayResponse processRequest(GatewayProxyRequest request, RequestContext context)
			throws BusinessException;

	@Override
	public GatewayResponse handleRequest(GatewayProxyRequest request, Context context) {
		String s = new StringBuilder("## ").append(this.getClass().getSimpleName()).append(".").append("handleRequest")
				.toString();
		logger.info("\n " + s + " Entrypoint ## "+request.toString());	
		GatewayResponse response = null;
		try {
			response = processRequest(request, null);
		} catch (BusinessException e) {			
			logger.info("Exception occured"+e.getMessage());
		}
		return response;
	}

	
	protected Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Custom-Header", "application/json");
		return headers;
	}

}
