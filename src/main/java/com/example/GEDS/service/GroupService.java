package com.example.GEDS.service;

import com.example.GEDS.dto.GroupFindRes;
import com.example.GEDS.dto.GroupNameChangeReq;
import com.example.GEDS.dto.GroupRequest;
import com.example.GEDS.dto.GroupResponse;
import com.example.GEDS.entity.Group;
import com.example.GEDS.entity.User;
import com.example.GEDS.exception.GroupNotFoundException;
import com.example.GEDS.exception.UserNotFoundException;
import com.example.GEDS.repository.GroupRepo;
import com.example.GEDS.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepo groupRepo;
    private final UserRepo userRepo;

    @Transactional
    public GroupResponse createGroup(GroupRequest req) {

        if (groupRepo.findByName(req.getName()).isPresent()) {
            throw new IllegalArgumentException("Group with name '" + req.getName() + "' already exists");
        }

        User user = userRepo.findById(req.getOwnerId()).orElseThrow(() ->
                new UserNotFoundException("User with id " + req.getOwnerId() + " not found"));

        Group group = Group.builder()
                .name(req.getName())
                .owner(user)
                .build();

        Group saved = groupRepo.save(group);

        return GroupResponse.builder()
                .groupName(saved.getName())
                .groupId(saved.getId())
                .ownerId(user.getId())
                .build();
    }

    @Transactional
    public String changeNameGroup(GroupNameChangeReq req) {
        Group group = groupRepo.findByName(req.getOldName())
                .orElseThrow(() -> new GroupNotFoundException("Group with name '" + req.getOldName() + "' not found"));

        if (groupRepo.findByName(req.getNewName()).isPresent()) {
            throw new IllegalArgumentException("Group with name '" + req.getNewName() + "' already exists");
        }

        group.rename(req.getNewName());
        groupRepo.save(group);

        return "Group renamed successfully from '" + req.getOldName() + "' to '" + req.getNewName() + "'";
    }

    @Transactional
    public String removeGroup(String name) {
        Group group = groupRepo.findByName(name)
                .orElseThrow(() -> new GroupNotFoundException("Group with name '" + name + "' not found"));

        groupRepo.delete(group);

        return "Group '" + name + "' deleted successfully";
    }


    public List<GroupFindRes> getMyGroups(Long userId) {
        return groupRepo.findByOwnerId(userId)
                .stream()
                .map(g -> GroupFindRes.builder()
                        .name(g.getName())
                        .id(g.getId())
                        .ownerId(g.getOwner().getId())
                        .build()
                )
                .toList();
    }

}
