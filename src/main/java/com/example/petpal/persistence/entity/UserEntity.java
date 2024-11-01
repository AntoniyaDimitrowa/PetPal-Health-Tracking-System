package com.example.petpal.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date memberSince;

    @NotBlank
    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String address;

    private String image;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ArrayList<PetEntity> pets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ArrayList<BreedHealthInfoEntity> breedHealthInfos;
}
