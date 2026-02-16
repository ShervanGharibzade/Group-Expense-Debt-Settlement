package com.example.GEDS.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupFindRes {
    private Long id;
    private String name;
    private Long ownerId;
}
