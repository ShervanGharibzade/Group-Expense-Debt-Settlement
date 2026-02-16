package com.example.GEDS.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users",
        indexes = {
        @Index(name = "idx_users_email", columnList = "email")
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 6, max = 30)
    @Column(nullable = false,length = 50)
    private String name;

    @NotBlank
    @Size(min = 6, max = 30)
    @Email
    @Size(max = 100)
    @Column(nullable = false,length = 50,unique = true)
    private String email;

    @NotBlank
    @Size(min = 60)
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(nullable = false,updatable = false, name = "created_at")
    private LocalDateTime createdAt;

}
