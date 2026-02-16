package com.example.GEDS.repository;

import com.example.GEDS.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepo extends JpaRepository<Group,Long> {

    Optional<Group> findByName(String name);

    List<Group> findByOwnerId(Long userId);
}
