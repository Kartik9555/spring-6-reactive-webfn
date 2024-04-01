package com.example.practice.spring6reactive.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Integer id;

    @NotEmpty
    @Size(min = 3, max = 255)
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
