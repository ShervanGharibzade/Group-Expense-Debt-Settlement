package com.example.GEDS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberReq {

    @NotBlank
    private String groupName;

    // FIX: @NotBlank is for Strings only; use @NotNull for Long
    @NotNull
    private Long memberId;
}
