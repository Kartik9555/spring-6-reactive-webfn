package com.example.practice.spring6reactive.mapper;

import com.example.practice.spring6reactive.domain.Beer;
import com.example.practice.spring6reactive.dto.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDTOToBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDTO(Beer beer);
}
