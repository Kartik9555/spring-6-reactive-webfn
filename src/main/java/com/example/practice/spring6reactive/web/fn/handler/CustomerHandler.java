package com.example.practice.spring6reactive.web.fn.handler;

import com.example.practice.spring6reactive.dto.CustomerDTO;
import com.example.practice.spring6reactive.service.CustomerService;
import com.example.practice.spring6reactive.web.fn.CustomerRoutesConfig;
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

@RequiredArgsConstructor
@Component
public class CustomerHandler {
    private final CustomerService customerService;

    private final Validator validator;
    private void validate(CustomerDTO customerDTO){
        Errors errors = new BeanPropertyBindingResult(customerDTO, "customerDTO");
        validator.validate(customerDTO, errors);
        if(errors.hasErrors()){
            throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        Flux<CustomerDTO> flux;
        if(request.queryParam("customerName").isPresent()){
            flux = customerService.findAllByCustomerName(request.queryParam("customerName").get());
        } else {
            flux = customerService.findAll();
        }
        return ServerResponse.ok()
            .body(flux, CustomerDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        Integer customerId = Integer.valueOf(request.pathVariable("customerId"));
        return ServerResponse.ok()
            .body(
                customerService.findById(customerId)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))),
                CustomerDTO.class
            );
    }

    public Mono<ServerResponse> save(ServerRequest request){
        return request.bodyToMono(CustomerDTO.class)
            .doOnNext(this::validate)
            .flatMap(customerService::save)
            .flatMap(savedDTO -> ServerResponse.created(
                UriComponentsBuilder.fromPath(CustomerRoutesConfig.CUSTOMER_PATH_ID).build(savedDTO.getId())
            ).build());
    }

    public Mono<ServerResponse> update(ServerRequest request){
        Integer customerId = Integer.valueOf(request.pathVariable("customerId"));
        return request.bodyToMono(CustomerDTO.class)
            .doOnNext(this::validate)
            .flatMap(customerDTO -> customerService.update(customerId, customerDTO))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .flatMap(customerDTO -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patch(ServerRequest request){
        Integer customerId = Integer.valueOf(request.pathVariable("customerId"));
        return request.bodyToMono(CustomerDTO.class)
            .doOnNext(this::validate)
            .flatMap(customerDTO -> customerService.patch(customerId, customerDTO))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .flatMap(customerDTO -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> deleteById(ServerRequest request){
        Integer customerId = Integer.valueOf(request.pathVariable("customerId"));
        return customerService.findById(customerId)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .map(customerDTO -> customerService.deleteById(customerDTO.getId()))
            .then(ServerResponse.noContent().build());
    }
}
