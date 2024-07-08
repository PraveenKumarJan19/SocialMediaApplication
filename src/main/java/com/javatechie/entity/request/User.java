package com.javatechie.entity.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "userinfo")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(1)
    private int userId;

    @NotEmpty(message = "UserName is mandatory")
    @Size(min = 2, max = 30, message = "UserName must be between 2 and 30 characters")
    private String username;

    @Column(name = "actualUserName", nullable = false)
    @NotEmpty(message = "ActualUserName is mandatory")
    @Size(min = 2, max = 30, message = "ActualUserName must be between 2 and 30 characters")
    private String actualUserName;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String password;

    private String profilePicture;
    private String bio;
    private String website;

    @NotEmpty(message = "Roles is mandatory")
    @Size(min = 2, message = "Roles should be at least 2 characters")
    private String roles;

    @NotEmpty(message = "CreatedAt is mandatory")
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String createdAt;

    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
    private Set<FollowRequest> receivedFollowRequests = new HashSet<>();

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
    private Set<FollowRequest> sentFollowRequests = new HashSet<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
    private Set<Follow> followers = new HashSet<>();

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
    private Set<Follow> following = new HashSet<>();
}
