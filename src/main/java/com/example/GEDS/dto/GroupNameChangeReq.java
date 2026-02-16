package com.example.GEDS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupNameChangeReq {

    @NotBlank
    @Size(min = 2, max = 100)
    private String oldName;

    @NotBlank
    @Size(min = 2, max = 100)
    private String newName;
}
