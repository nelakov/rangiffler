package com.elakov.rangiffler.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString(exclude = "users")
@RequiredArgsConstructor
@Entity
@Table(name = "users", catalog = "userdata")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(unique = true)
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "surname")
    private String surname;

    @Column(name = "avatar", columnDefinition = "LONGBLOB")
    private byte[] avatar;


}