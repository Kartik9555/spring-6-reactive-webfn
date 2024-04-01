package com.example.practice.spring6reactive.service;

import com.example.practice.spring6reactive.dto.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerDTO> findAll();

    Flux<CustomerDTO> findAllByCustomerName(String name);

    Mono<CustomerDTO> findById(Integer customerId);

    Mono<CustomerDTO> save(CustomerDTO customerDTO);

    Mono<CustomerDTO> update(Integer customerId, CustomerDTO customerDTO);

    Mono<CustomerDTO> patch(Integer customerId, CustomerDTO customerDTO);

    Mono<Void> deleteById(Integer customerId);
}
