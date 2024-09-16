package com.example.petpal.persistence.entity;

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
}
