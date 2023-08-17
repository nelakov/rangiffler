package com.elakov.rangiffler.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString(exclude = "photos")
@RequiredArgsConstructor
@Entity
@Table(name = "photos")
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;
}
