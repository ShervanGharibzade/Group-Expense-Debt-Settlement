package com.example.GEDS.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRequest {

    private String name;
    private Long ownerId;

}
