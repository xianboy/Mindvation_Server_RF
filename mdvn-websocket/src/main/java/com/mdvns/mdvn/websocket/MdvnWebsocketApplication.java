package com.mdvns.mdvn.websocket;


import com.mdvns.mdvn.common.beans.RestResponse;
import com.mdvns.mdvn.websocket.service.WebSocketService;
import com.mdvns.mdvn.websocket.service.impl.MyWebSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class MdvnWebsocketApplication {
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		// Do any additional configuration here
		return builder.build();
	}
	@Bean
	public RestResponse restDefaultResponse() {
		return new RestResponse();
	}

	@Bean
	public WebSocketService webSocketService() {
		return new MyWebSocket();
	}

	public static void main(String[] args) {
		SpringApplication.run(MdvnWebsocketApplication.class, args);
	}
}
