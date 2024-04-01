package com.example.practice.spring6reactive.web.fn.handler;

import com.example.practice.spring6reactive.dto.BeerDTO;
import com.example.practice.spring6reactive.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.example.practice.spring6reactive.web.fn.BeerRoutesConfig.BEER_PATH_ID;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;
    private final Validator validator;

    private void validate(BeerDTO beerDTO){
        Errors errors = new BeanPropertyBindingResult(beerDTO, "beerDTO");
        validator.validate(beerDTO, errors);
        if(errors.hasErrors()){
            throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> listBeer(ServerRequest request) {
        Flux<BeerDTO> flux;
        if(request.queryParam("beerStyle").isPresent()){
            flux = beerService.findByBeerStyle(request.queryParam("beerStyle").get());
        } else {
            flux = beerService.findAll();
        }
        return ServerResponse.ok()
            .body(flux, BeerDTO.class);
    }


    public Mono<ServerResponse> findById(ServerRequest request) {
        Integer beerId = Integer.valueOf(request.pathVariable("beerId"));
        return ServerResponse.ok()
            .body(
                beerService.findById(beerId)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))),
                BeerDTO.class
            );
    }


    public Mono<ServerResponse> save(ServerRequest request) {
        return beerService.save(request.bodyToMono(BeerDTO.class).doOnNext(this::validate))
            .flatMap(beerDTO -> ServerResponse.created(
                UriComponentsBuilder.fromPath(BEER_PATH_ID)
                    .build(beerDTO.getId())
            ).build());

    }


    public Mono<ServerResponse> update(ServerRequest request) {
        Integer beerId = Integer.valueOf(request.pathVariable("beerId"));
        return request.bodyToMono(BeerDTO.class)
            .doOnNext(this::validate)
            .flatMap(beerDTO -> beerService.update(beerId, beerDTO))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .flatMap(savedBeer -> ServerResponse.noContent().build());
    }


    public Mono<ServerResponse> patch(ServerRequest request) {
        Integer beerId = Integer.valueOf(request.pathVariable("beerId"));
        return request.bodyToMono(BeerDTO.class)
            .doOnNext(this::validate)
            .flatMap(beerDTO -> beerService.patch(beerId, beerDTO))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .flatMap(savedDTO -> ServerResponse.noContent().build());
    }


    public Mono<ServerResponse> deleteById(ServerRequest request) {
        Integer beerId = Integer.valueOf(request.pathVariable("beerId"));
        return beerService.findById(beerId)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(beerDTO -> beerService.deleteById(beerDTO.getId()))
            .then(ServerResponse.noContent().build());
    }
}
