package com.example.practice.spring6reactive.mapper;

import com.example.practice.spring6reactive.domain.Customer;
import com.example.practice.spring6reactive.dto.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDTOToCustomer(CustomerDTO customerDTO);
    CustomerDTO customerToCustomerDTO(Customer customer);
}
