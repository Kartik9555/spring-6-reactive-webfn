package com.example.practice.spring6reactive.repositories;

import com.example.practice.spring6reactive.domain.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
    Flux<Customer> findByNameIsLikeIgnoreCase(String beerName);
}