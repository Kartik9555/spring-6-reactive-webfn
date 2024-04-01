package com.example.practice.spring6reactive.service;

import com.example.practice.spring6reactive.dto.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {
    Flux<BeerDTO> findAll();

    Flux<BeerDTO> findByBeerName(String beerName);

    Flux<BeerDTO> findByBeerStyle(String beerStyle);

    Mono<BeerDTO> findById(Integer beerId);

    Mono<BeerDTO> save(Mono<BeerDTO> beerDTO);

    Mono<BeerDTO> update(Integer beerId, BeerDTO beerDTO);

    Mono<BeerDTO> patch(Integer beerId, BeerDTO beerDTO);

    Mono<Void> deleteById(Integer beerId);
}
