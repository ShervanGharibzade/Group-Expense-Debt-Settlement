package com.example.GEDS.controller;


import com.example.GEDS.dto.GroupMemberReq;
import com.example.GEDS.dto.GroupMemberRes;
import com.example.GEDS.entity.GroupMember;
import com.example.GEDS.service.GroupMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class GroupMemberController {

    private final GroupMemberService groupMemberService;


    @GetMapping("/{name}")
    public List<GroupMember> getAll(@PathVariable String name) {
        return groupMemberService.getAllMembers(name);
    }

    @PostMapping
    public ResponseEntity<GroupMemberRes> add(@RequestBody @Valid GroupMemberReq req) {

        GroupMemberRes message = groupMemberService.addMember(req);

        return ResponseEntity.ok(message);
    }

    @PostMapping("/{ownerId}")
    public ResponseEntity<GroupMemberRes> delete(@PathVariable Long ownerId, @RequestBody @Valid GroupMemberReq req) {

        GroupMemberRes message = groupMemberService.removeMember(req,ownerId);

        return ResponseEntity.ok(message);
    }
}
