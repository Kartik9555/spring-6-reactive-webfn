package com.example.practice.spring6reactive.web.fn;

import com.example.practice.spring6reactive.web.fn.handler.BeerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class BeerRoutesConfig {
    public static final String BEER_PATH = "/api/v3/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerHandler handler;


    @Bean
    public RouterFunction<ServerResponse> beerRoutes() {
        return route()
            .GET(BEER_PATH, accept(APPLICATION_JSON), handler::listBeer)
            .GET(BEER_PATH_ID, accept(APPLICATION_JSON), handler::findById)
            .POST(BEER_PATH, accept(APPLICATION_JSON), handler::save)
            .PUT(BEER_PATH_ID, accept(APPLICATION_JSON), handler::update)
            .PATCH(BEER_PATH_ID, accept(APPLICATION_JSON), handler::patch)
            .DELETE(BEER_PATH_ID, accept(APPLICATION_JSON), handler::deleteById)
            .build();
    }
}
