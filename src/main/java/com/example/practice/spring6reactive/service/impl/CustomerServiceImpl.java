package com.example.practice.spring6reactive.service.impl;

import com.example.practice.spring6reactive.dto.CustomerDTO;
import com.example.practice.spring6reactive.mapper.CustomerMapper;
import com.example.practice.spring6reactive.repositories.CustomerRepository;
import com.example.practice.spring6reactive.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    @Override
    public Flux<CustomerDTO> findAll() {
        return customerRepository.findAll()
            .map(customerMapper::customerToCustomerDTO);
    }


    @Override
    public Flux<CustomerDTO> findAllByCustomerName(String name) {
        return customerRepository.findByNameIsLikeIgnoreCase("%" + name + "%")
            .map(customerMapper::customerToCustomerDTO);
    }


    @Override
    public Mono<CustomerDTO> findById(Integer customerId) {
        return customerRepository.findById(customerId)
            .map(customerMapper::customerToCustomerDTO);
    }


    @Override
    public Mono<CustomerDTO> save(CustomerDTO customerDTO) {
        return customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO))
            .map(customerMapper::customerToCustomerDTO);
    }


    @Override
    public Mono<CustomerDTO> update(Integer customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
            .map(customer -> {
                customer.setName(customerDTO.getName());
                return customer;
            }).flatMap(customerRepository::save)
            .map(customerMapper::customerToCustomerDTO);
    }


    @Override
    public Mono<CustomerDTO> patch(Integer customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
            .map(customer -> {
                if (StringUtils.hasLength(customerDTO.getName())) {
                    customer.setName(customerDTO.getName());
                }
                return customer;
            }).flatMap(customerRepository::save)
            .map(customerMapper::customerToCustomerDTO);
    }


    @Override
    public Mono<Void> deleteById(Integer customerId) {
        return customerRepository.deleteById(customerId);
    }
}
