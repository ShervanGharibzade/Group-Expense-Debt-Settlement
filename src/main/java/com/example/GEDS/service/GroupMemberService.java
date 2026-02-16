package com.example.GEDS.service;

import com.example.GEDS.config.UserNotFoundException;
import com.example.GEDS.dto.GroupMemberReq;
import com.example.GEDS.dto.GroupMemberRes;
import com.example.GEDS.dto.GroupNameChangeReq;
import com.example.GEDS.entity.Group;
import com.example.GEDS.entity.GroupMember;
import com.example.GEDS.entity.User;
import com.example.GEDS.repository.GroupMemberRepo;
import com.example.GEDS.repository.GroupRepo;
import com.example.GEDS.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepo groupMemberRepo;
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;

    public List<GroupMember> getAllMembers(String groupName) {

        Group group = groupRepo.findByName(groupName)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        return groupMemberRepo.findByGroupId(group.getId());
    }


    @Transactional
    public GroupMemberRes addMember(GroupMemberReq req){
        User user = userRepo.findById(req.getMemberId()).orElseThrow(()->
                new UserNotFoundException("User not found"));

        Group group = groupRepo.findByName(req.getGroupName()).orElseThrow(()->
                new IllegalArgumentException("Group not found"));

        GroupMember gm = GroupMember.builder()
                .group(group)
                .member(user)
                .build();

        groupMemberRepo.save(gm);

        return GroupMemberRes.builder()
                .message("Member Added to Group Successfuly")
                .build();
    }

    @Transactional
    public GroupMemberRes removeMember(GroupMemberReq req,Long ownerId){

        User user = userRepo.findById(req.getMemberId()).orElseThrow(()->
                new UserNotFoundException("User not found"));

        Group group = groupRepo.findByName(req.getGroupName()).orElseThrow(()->
                new IllegalArgumentException("Group not found"));

        if (!group.getOwnerId().getId().equals(ownerId)) {
            throw new UserNotFoundException("Only the owner can remove members");
        }

        GroupMember member = groupMemberRepo.findByGroupIdAndUserId(group.getId(), user.getId())
                .orElseThrow(()->new IllegalArgumentException("Member not Found"));

        groupMemberRepo.delete(member);

        return GroupMemberRes.builder()
                .message("Member removed successfully")
                .build();

    }

}
