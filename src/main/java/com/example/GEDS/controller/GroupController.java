package com.example.GEDS.controller;

import com.example.GEDS.dto.GroupFindRes;
import com.example.GEDS.dto.GroupNameChangeReq;
import com.example.GEDS.dto.GroupRequest;
import com.example.GEDS.dto.GroupResponse;
import com.example.GEDS.entity.Group;
import com.example.GEDS.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController()
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<GroupResponse> create(@RequestBody @Valid GroupRequest groupRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createGroup(groupRequest));
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody @Valid GroupNameChangeReq groupRequest) {
        return ResponseEntity.ok(groupService.changeNameGroup(groupRequest));
    }


    @GetMapping("/{id}")
    public List<GroupFindRes> myGroups (@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getMyGroups(id)).getBody();
    }
    @DeleteMapping("/{name}")
    public ResponseEntity<String> delete(@PathVariable String name) {
        return ResponseEntity.ok(groupService.removeGroup(name));
    }
}
