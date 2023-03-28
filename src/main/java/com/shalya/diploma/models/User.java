package com.shalya.diploma.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.shalya.diploma.security.models.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@JsonIgnoreProperties("password")
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private java.util.List<ShopList> lists;

    public User() {
        role = Role.USER;
    }

}