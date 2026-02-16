package com.example.GEDS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupResponse {

    @NotBlank
    private String groupName;

    @NotBlank
    private Long groupId;

    @NotBlank
    private Long ownerId;
}
