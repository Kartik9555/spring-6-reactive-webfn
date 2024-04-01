package com.example.practice.spring6reactive.controller;

import com.example.practice.spring6reactive.dto.BeerDTO;
import com.example.practice.spring6reactive.dto.CustomerDTO;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static com.example.practice.spring6reactive.web.fn.CustomerRoutesConfig.CUSTOMER_PATH;
import static com.example.practice.spring6reactive.web.fn.CustomerRoutesConfig.CUSTOMER_PATH_ID;
import static org.hamcrest.Matchers.greaterThan;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(10)
    void findAll() {
        webTestClient.get().uri(CUSTOMER_PATH)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Content-type", "application/json")
            .expectBody().jsonPath("$.size()").value(greaterThan(1));
    }

    @Test
    @Order(15)
    void findAllByCustomerName() {
        final String name = "TEST";
        CustomerDTO testDto = getCustomer();
        testDto.setName(name);

        //create test data
        webTestClient.post().uri(CUSTOMER_PATH)
            .body(Mono.just(testDto), CustomerDTO.class)
            .header("Content-Type", "application/json")
            .exchange();

        webTestClient.get().uri(UriComponentsBuilder
                .fromPath(CUSTOMER_PATH)
                .queryParam("customerName", name).build().toUri())
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Content-type", "application/json")
            .expectBody().jsonPath("$.size()").value(IsEqual.equalTo(1));
    }

    @Test
    @Order(20)
    void findById() {
        webTestClient.get().uri(CUSTOMER_PATH_ID, 1)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Content-type", "application/json")
            .expectBody(CustomerDTO.class);
    }

    @Test
    @Order(25)
    void findByIdNotFound() {
        webTestClient.get().uri(CUSTOMER_PATH_ID, 100)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(30)
    void save() {
        webTestClient.post().uri(CUSTOMER_PATH)
            .body(Mono.just(getCustomer()), CustomerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    @Order(35)
    void saveBadRequest() {
        CustomerDTO customerDTO = getCustomer();
        customerDTO.setName("");
        webTestClient.post().uri(CUSTOMER_PATH)
            .body(Mono.just(customerDTO), CustomerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(40)
    void update() {
        webTestClient.put().uri(CUSTOMER_PATH_ID, 1)
            .body(Mono.just(getCustomer()), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(44)
    void updateBadRequest() {
        CustomerDTO customerDTO = getCustomer();
        customerDTO.setName("");
        webTestClient.put().uri(CUSTOMER_PATH_ID, 1)
            .body(Mono.just(customerDTO), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    @Order(40)
    void updateNotFound() {
        webTestClient.put().uri(CUSTOMER_PATH_ID, 100)
            .body(Mono.just(getCustomer()), BeerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(50)
    void patch() {
        webTestClient.patch().uri(CUSTOMER_PATH_ID, 1)
            .body(Mono.just(getCustomer()), CustomerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(54)
    void patchNotFound() {
        webTestClient.patch().uri(CUSTOMER_PATH_ID, 100)
            .body(Mono.just(getCustomer()), CustomerDTO.class)
            .header("Content-type", "application/json")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    @Order(9999)
    void deleteById() {
        webTestClient.delete().uri(CUSTOMER_PATH_ID, 1)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    @Order(99999)
    void deleteByIdNotFound() {
        webTestClient.delete().uri(CUSTOMER_PATH_ID, 100)
            .exchange()
            .expectStatus().isNotFound();
    }

    CustomerDTO getCustomer(){
        return CustomerDTO.builder()
            .name("Jim")
            .build();
    }
}