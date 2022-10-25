package com.taskmate.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.estockmarket.exception.BusinessException;
import com.estockmarket.handler.TaskMateRequestHandler;
import com.estockmarket.model.GatewayProxyRequest;
import com.estockmarket.model.GatewayResponse;
import com.estockmarket.model.RequestContext;
import com.fasterxml.jackson.core.JsonProcessingException;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TaskMateRequestHandlerTest {

	@InjectMocks
	TaskMateRequestHandler taskMateRequestHandler;
	private RequestContext context;
	private GatewayProxyRequest request;

	// @Test
	public void successfulResponse() {
		TaskMateRequestHandler app = new TaskMateRequestHandler();
		GatewayResponse result = (GatewayResponse) app.handleRequest(null, null);
		assertEquals(result.getStatusCode(), 200);
		assertEquals(result.getHeaders().get("Content-Type"), "application/json");
		String content = result.getBody();
		assertNotNull(content);
		assertTrue(content.contains("\"message\""));
		assertTrue(content.contains("\"hello world\""));
		assertTrue(content.contains("\"location\""));
	}

	@Test
	void testGetConfiguration() {
		try {
			request = new GatewayProxyRequest();
			request.setAction("dummy");
			request.setBody("{\"email\":\"admin@taskmate.com\",\"password\":\"password\", \"action\":\"login.user\"}");
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
