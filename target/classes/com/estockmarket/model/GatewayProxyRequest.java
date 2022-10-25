package com.estockmarket.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class GatewayProxyRequest {
	
	private String resourse;
	private String path;
	private String httpMethod;
	private Map<String, String> headers;
	private Map<String, String> pathParameters;
	private Map<String, String> queryParameters;
	private String body;
	private String methodArn;
	private String invoker;
	private RequestContext requestContext;
	private String action;	
}
