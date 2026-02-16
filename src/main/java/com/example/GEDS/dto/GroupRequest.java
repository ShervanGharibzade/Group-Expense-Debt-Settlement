package com.example.GEDS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    private Long ownerId;
}
