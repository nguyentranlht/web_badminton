package com.example.webBadminton.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "description")
    private String description;

    // Ward information
    @Column(name = "ward_code")
    private String wardCode;

    // Ward information
    @Column(name = "district_code")
    private String districtCode;

    // Ward information
    @Column(name = "province_code")
    private String provinceCode;
}
