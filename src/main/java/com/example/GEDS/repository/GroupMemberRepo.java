package com.example.GEDS.repository;

import com.example.GEDS.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepo extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findByGroupId(Long groupId);

    // "member" is the field name in GroupMember entity (JoinColumn = member_id)
    Optional<GroupMember> findByGroupIdAndMemberId(Long groupId, Long memberId);

    boolean existsByGroupIdAndMemberId(Long groupId, Long memberId);
}
