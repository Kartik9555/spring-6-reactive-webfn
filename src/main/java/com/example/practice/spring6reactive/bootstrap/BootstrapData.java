package com.example.practice.spring6reactive.bootstrap;

import com.example.practice.spring6reactive.domain.Beer;
import com.example.practice.spring6reactive.domain.Customer;
import com.example.practice.spring6reactive.repositories.BeerRepository;
import com.example.practice.spring6reactive.repositories.CustomerRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;


    @Override
    public void run(String... args) throws Exception {
        loadBeer();
        beerRepository.count().subscribe(count -> {
            System.out.println("Beers available: " + count);
        });
        loadCustomer();
        customerRepository.count().subscribe(count -> {
            System.out.println("Customers available: " + count);
        });
    }


    private void loadCustomer() {
        customerRepository.count().subscribe(count -> {
            if(count == 0){
                Customer customer1 = Customer.builder()
                    .name("Max Muller")
                    .build();

                Customer customer2 = Customer.builder()
                    .name("Henry Ford")
                    .build();

                Customer customer3 = Customer.builder()
                    .name("Pam Hilton")
                    .build();

                customerRepository.save(customer1).subscribe();
                customerRepository.save(customer2).subscribe();
                customerRepository.save(customer3).subscribe();
            }
        });
    }


    private void loadBeer() {
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                Beer beer1 = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle("PALE ALE")
                    .upc("12345")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(122)
                    .build();

                Beer beer2 = Beer.builder()
                    .beerName("Crank")
                    .beerStyle("PALE ALE")
                    .upc("123456222")
                    .price(new BigDecimal("11.99"))
                    .quantityOnHand(392)
                    .build();

                Beer beer3 = Beer.builder()
                    .beerName("Sunshine City")
                    .beerStyle("IPA")
                    .upc("12356")
                    .price(new BigDecimal("13.99"))
                    .quantityOnHand(144)
                    .build();

                beerRepository.save(beer1).subscribe();
                beerRepository.save(beer2).subscribe();
                beerRepository.save(beer3).subscribe();
            }
        });
    }

}
