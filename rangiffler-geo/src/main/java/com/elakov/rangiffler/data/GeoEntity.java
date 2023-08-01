package com.elakov.rangiffler.data;

import jakarta.persistence.*;

@Entity
@Table(name = "country")
public class GeoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
    private Long id;

}
