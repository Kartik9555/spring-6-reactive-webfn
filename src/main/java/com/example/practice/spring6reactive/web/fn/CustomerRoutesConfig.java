package com.example.practice.spring6reactive.web.fn;

import com.example.practice.spring6reactive.web.fn.handler.CustomerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@RequiredArgsConstructor
@Configuration
public class CustomerRoutesConfig {

    public static final String CUSTOMER_PATH = "/api/v3/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";
    private final CustomerHandler handler;

    @Bean
    public RouterFunction<ServerResponse> customerRoutes(){
        return route()
            .GET(CUSTOMER_PATH, accept(APPLICATION_JSON), handler::findAll)
            .GET(CUSTOMER_PATH_ID, accept(APPLICATION_JSON), handler::findById)
            .POST(CUSTOMER_PATH, accept(APPLICATION_JSON), handler::save)
            .PUT(CUSTOMER_PATH_ID, accept(APPLICATION_JSON), handler::update)
            .PATCH(CUSTOMER_PATH_ID, accept(APPLICATION_JSON), handler::patch)
            .DELETE(CUSTOMER_PATH_ID, accept(APPLICATION_JSON), handler::deleteById)
            .build();
    }
}
