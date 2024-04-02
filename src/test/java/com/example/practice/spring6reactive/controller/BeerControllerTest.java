package com.example.practice.spring6reactive.controller;

import com.example.practice.spring6reactive.dto.BeerDTO;
import java.math.BigDecimal;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static com.example.practice.spring6reactive.web.fn.BeerRoutesConfig.BEER_PATH;
import static com.example.practice.spring6reactive.web.fn.BeerRoutesConfig.BEER_PATH_ID;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(10)
    void listBeer() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .get()
            .uri(BEER_PATH)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals(CONTENT_TYPE, "application/json")
            .expectBody().jsonPath("$.size()").value(Matchers.greaterThan(1));
    }

    @Test
    @Order(14)
    void listBeerByBeerStyle() {
        final String BEER_STYLE = "TEST";
        BeerDTO testDto = getBeer();
        testDto.setBeerStyle(BEER_STYLE);

        //create test data
        webTestClient
            .mutateWith(mockOAuth2Login())
            .post()
            .uri(BEER_PATH)
            .body(Mono.just(testDto), BeerDTO.class)
            .header("Content-Type", "application/json")
            .exchange();

        webTestClient
            .mutateWith(mockOAuth2Login())
            .get()
            .uri(UriComponentsBuilder
                .fromPath(BEER_PATH)
                .queryParam("beerStyle", BEER_STYLE).build().toUri())
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Content-type", "application/json")
            .expectBody().jsonPath("$.size()").value(IsEqual.equalTo(1));
    }

    @Test
    @Order(20)
    void findById() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .get()
            .uri(BEER_PATH_ID, 1)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals(CONTENT_TYPE, "application/json")
            .expectBody(BeerDTO.class);
    }

    @Test
    @Order(25)
    void findByIdNotFound() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .get()
            .uri(BEER_PATH_ID, 9999)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(30)
    void saveBeer() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .post()
            .uri(BEER_PATH)
            .body(Mono.just(getBeer()), BeerDTO.class)
            .header(CONTENT_TYPE, "application/json")
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().exists(LOCATION);
    }

    @Test
    @Order(35)
    void saveBeerBadRequest() {
        BeerDTO beerDTO = getBeer();
        beerDTO.setBeerName("");
        webTestClient
            .mutateWith(mockOAuth2Login())
            .post()
            .uri(BEER_PATH)
            .body(Mono.just(beerDTO), BeerDTO.class)
            .header(CONTENT_TYPE, "application/json")
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(40)
    void updateBeer() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .put()
            .uri(BEER_PATH_ID, 1)
            .body(Mono.just(getBeer()), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(44)
    void updateBeerBadRequest() {
        BeerDTO beerDTO = getBeer();
        beerDTO.setBeerName("");
        webTestClient
            .mutateWith(mockOAuth2Login())
            .put()
            .uri(BEER_PATH_ID, 1)
            .body(Mono.just(beerDTO), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(48)
    void updateBeerNotFound() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .put()
            .uri(BEER_PATH_ID, 100)
            .body(Mono.just(getBeer()), BeerDTO.class)
            .header(CONTENT_TYPE, "application/json")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(50)
    void patchBeer() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .patch()
            .uri(BEER_PATH_ID, 1)
            .body(Mono.just(getBeer()), BeerDTO.class)
            .header(CONTENT_TYPE, "application/json")
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(54)
    void patchBeerBadRequest() {
        BeerDTO beerDTO = getBeer();
        beerDTO.setBeerName("");
        webTestClient
            .mutateWith(mockOAuth2Login())
            .patch()
            .uri(BEER_PATH_ID, 1)
            .body(Mono.just(beerDTO), BeerDTO.class)
            .header(CONTENT_TYPE, "application/json")
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(58)
    void patchBeerNotFound() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .patch()
            .uri(BEER_PATH_ID, 100)
            .body(Mono.just(getBeer()), BeerDTO.class)
            .header(CONTENT_TYPE, "application/json")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(9999)
    void deleteById() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .delete()
            .uri(BEER_PATH_ID, 1)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(99999)
    void deleteByIdNotFound() {
        webTestClient
            .mutateWith(mockOAuth2Login())
            .delete()
            .uri(BEER_PATH_ID, 1000)
            .exchange()
            .expectStatus().isNotFound();
    }

    BeerDTO getBeer(){
        return BeerDTO.builder()
            .beerName("Monster Feel")
            .beerStyle("IPA")
            .upc("1223345")
            .price(BigDecimal.TEN)
            .quantityOnHand(10)
            .build();
    }
}