package com.example.GEDS.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMemberRes {

    private String message;
    private Long memberId;
    private String memberName;
    private String groupName;
    private LocalDateTime joinedAt;
}
