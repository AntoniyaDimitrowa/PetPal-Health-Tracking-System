package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.Pet;
import lombok.*;

import java.util.ArrayList;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name = "users")
public class UserEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false, unique = true)
    private String username;

//    @Column(nullable = false)
    private String password;

//    @Column(nullable = false, unique = true)
    private String email;
    private String role;

    private Optional<String> address;
    private Optional<ArrayList<Pet>> pets;
}
