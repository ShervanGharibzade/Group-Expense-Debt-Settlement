package com.example.GEDS.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMemberRes {

    @NotBlank
    private String message;
}
