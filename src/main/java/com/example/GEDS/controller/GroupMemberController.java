package com.example.GEDS.controller;

import com.example.GEDS.dto.GroupMemberReq;
import com.example.GEDS.dto.GroupMemberRes;
import com.example.GEDS.service.GroupMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    // FIX 24: Returns List<GroupMemberRes> DTO, not raw entity
    @GetMapping("/{name}")
    public ResponseEntity<List<GroupMemberRes>> getAll(@PathVariable String name) {
        return ResponseEntity.ok(groupMemberService.getAllMembers(name));
    }

    @PostMapping
    public ResponseEntity<GroupMemberRes> add(@RequestBody @Valid GroupMemberReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupMemberService.addMember(req));
    }

    // FIX 10: Was @PostMapping — a delete operation must be @DeleteMapping
    @DeleteMapping("/{ownerId}")
    public ResponseEntity<GroupMemberRes> delete(
            @PathVariable Long ownerId,
            @RequestBody @Valid GroupMemberReq req) {
        return ResponseEntity.ok(groupMemberService.removeMember(req, ownerId));
    }
}
