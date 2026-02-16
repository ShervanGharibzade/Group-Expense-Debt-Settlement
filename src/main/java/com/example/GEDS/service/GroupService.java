package com.example.GEDS.service;

import com.example.GEDS.config.UserNotFoundException;
import com.example.GEDS.dto.GroupNameChangeReq;
import com.example.GEDS.dto.GroupRequest;
import com.example.GEDS.dto.GroupResponse;
import com.example.GEDS.entity.Group;
import com.example.GEDS.entity.User;
import com.example.GEDS.repository.GroupRepo;
import com.example.GEDS.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;


    public GroupResponse createGroup(GroupRequest req) {

        if (groupRepo.findByName(req.getName()).isPresent()) {
            throw new IllegalArgumentException("Group with name " + req.getName() + " already exists");
        }

        User user = userRepo.findById(req.getOwnerId()).orElseThrow(()->
                new UserNotFoundException("User with id " + req.getOwnerId() + " not found"));

        Group group = Group.builder()
                .name(req.getName())
                .ownerId(user)
                .build();

        groupRepo.save(group);

        return GroupResponse.builder()
                .groupName(group.getName())
                .ownerId(user.getId())
                .build();
    }

    public String changeNameGroup(GroupNameChangeReq req) {
        Group group = groupRepo.findByName(req.getOldName())
                .orElseThrow(() -> new IllegalArgumentException("Group with this name not found"));

        group.rename(req.getNewName());

        groupRepo.save(group);

        return "Group name change Successfully";
    }


    public String removeGroup(String name) {
        Group group = groupRepo.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Group with this name not found"));

        groupRepo.delete(group);

        return "Group deleted successfully";
    }

}
