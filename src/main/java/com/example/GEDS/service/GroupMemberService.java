package com.example.GEDS.service;

import com.example.GEDS.dto.GroupMemberReq;
import com.example.GEDS.dto.GroupMemberRes;
import com.example.GEDS.entity.Group;
import com.example.GEDS.entity.GroupMember;
import com.example.GEDS.entity.User;
import com.example.GEDS.exception.GroupNotFoundException;
import com.example.GEDS.exception.UserNotFoundException;
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

    // FIX 24: Return DTOs, not raw GroupMember entities (avoids Jackson serialization of lazy proxies)
    public List<GroupMemberRes> getAllMembers(String groupName) {

        Group group = groupRepo.findByName(groupName)
                .orElseThrow(() -> new GroupNotFoundException("Group '" + groupName + "' not found"));

        return groupMemberRepo.findByGroupId(group.getId())
                .stream()
                .map(gm -> GroupMemberRes.builder()
                        .memberId(gm.getMember().getId())
                        .memberName(gm.getMember().getName())
                        .groupName(group.getName())
                        .joinedAt(gm.getJoinedAt())
                        .build())
                .toList();
    }

    @Transactional
    public GroupMemberRes addMember(GroupMemberReq req) {

        User user = userRepo.findById(req.getMemberId()).orElseThrow(() ->
                new UserNotFoundException("User with id " + req.getMemberId() + " not found"));

        Group group = groupRepo.findByName(req.getGroupName()).orElseThrow(() ->
                new GroupNotFoundException("Group '" + req.getGroupName() + "' not found"));

        // FIX 2: Correct method name to match entity field "member"
        if (groupMemberRepo.existsByGroupIdAndMemberId(group.getId(), user.getId())) {
            throw new IllegalStateException("User is already a member of this group");
        }

        GroupMember gm = GroupMember.builder()
                .group(group)
                .member(user)
                .build();

        GroupMember saved = groupMemberRepo.save(gm);

        return GroupMemberRes.builder()
                .message("Member added to group successfully")
                .memberId(user.getId())
                .memberName(user.getName())
                .groupName(group.getName())
                .joinedAt(saved.getJoinedAt())
                .build();
    }

    @Transactional
    public GroupMemberRes removeMember(GroupMemberReq req, Long ownerId) {

        User user = userRepo.findById(req.getMemberId()).orElseThrow(() ->
                new UserNotFoundException("User with id " + req.getMemberId() + " not found"));

        Group group = groupRepo.findByName(req.getGroupName()).orElseThrow(() ->
                new GroupNotFoundException("Group '" + req.getGroupName() + "' not found"));

        // FIX: Field renamed from ownerId to owner in Group entity
        if (!group.getOwner().getId().equals(ownerId)) {
            throw new IllegalArgumentException("Only the group owner can remove members");
        }

        // FIX 2: Correct method name (findByGroupIdAndMemberId, not findByGroupIdAndUserId)
        GroupMember member = groupMemberRepo.findByGroupIdAndMemberId(group.getId(), user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User is not a member of this group"));

        groupMemberRepo.delete(member);

        return GroupMemberRes.builder()
                .message("Member removed successfully")
                .memberId(user.getId())
                .memberName(user.getName())
                .groupName(group.getName())
                .build();
    }
}
