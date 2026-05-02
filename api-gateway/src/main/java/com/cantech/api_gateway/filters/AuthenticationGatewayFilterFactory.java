package com.cantech.api_gateway.filters;

import com.cantech.api_gateway.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    public AuthenticationGatewayFilterFactory(){
        super(Config.class);
    }

    @Autowired
    private JwtService jwtService;


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String requestHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if(requestHeader == null){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();  //The request will not move forward
            }

            String token = requestHeader.split("Bearer ")[1];
            Long userId = jwtService.userIdFromToken(token);

            exchange.getRequest()
                    .mutate()
                    .header("user-id", userId.toString())
                    .build();

            return chain.filter(exchange);
        };
    }

    public static class Config{

    }
}
