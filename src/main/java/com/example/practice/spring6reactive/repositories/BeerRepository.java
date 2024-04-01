package com.example.practice.spring6reactive.repositories;

import com.example.practice.spring6reactive.domain.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BeerRepository extends ReactiveCrudRepository<Beer, Integer> {
    Flux<Beer> findByBeerNameIsLikeIgnoreCase(String beerName);
    Flux<Beer> findByBeerStyle(String beerStyle);
}
