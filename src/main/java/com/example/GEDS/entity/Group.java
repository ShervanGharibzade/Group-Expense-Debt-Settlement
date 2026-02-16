package com.example.GEDS.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "groups",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "ownerId"})
        },
        indexes = {
                @Index(name = "idx_groups_owner_id", columnList = "ownerId")
        })
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Group {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 6, max = 100)
    @Column(nullable = false,length = 100,unique = true)
    private String name;

    @JsonIgnoreProperties({"groups", "password", "email"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User ownerId;


    @CreationTimestamp
    @Column(nullable = false, updatable = false,name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false,name = "updated_at")
    private LocalDateTime updatedAt;


    public void rename(String name) {
        this.name = name;
    }
}
