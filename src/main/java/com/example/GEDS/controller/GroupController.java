package com.example.GEDS.controller;

import com.example.GEDS.dto.GroupNameChangeReq;
import com.example.GEDS.dto.GroupRequest;
import com.example.GEDS.dto.GroupResponse;
import com.example.GEDS.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public GroupResponse create(@RequestBody @Valid GroupRequest groupRequest) {

        return  groupService.createGroup(groupRequest);
    }

    @PutMapping
    public String update(@RequestBody @Valid GroupNameChangeReq groupRequest) {

        return groupService.changeNameGroup(groupRequest);
    }

    @DeleteMapping("/{name}")
    public String delete(@PathVariable @Valid String name) {
        return groupService.removeGroup(name);
    }
}
