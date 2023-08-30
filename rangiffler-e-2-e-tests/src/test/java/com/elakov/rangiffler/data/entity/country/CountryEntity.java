package com.elakov.rangiffler.data.entity.country;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@Entity
@Table(name = "country")
public class CountryEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "country_code", nullable = false, unique = true)
    private String code;

    @Column(name = "country_name",nullable = false, unique = true)
    private String name;

}
