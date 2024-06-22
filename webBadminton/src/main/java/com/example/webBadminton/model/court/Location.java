package com.example.webBadminton.model;

import com.example.webBadminton.model.location.District;
import com.example.webBadminton.model.location.Province;
import com.example.webBadminton.model.location.Ward;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Không được để trống địa chỉ")
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "description")
    private String description;

    // Ward information
    @NotBlank(message = "Không được để trống địa chỉ")
    private String wardId;
    private String wardName;

    // District information
    @NotBlank(message = "Không được để trống địa chỉ")
    private String districtId;
    private String districtName;

    // Province information
    @NotBlank(message = "Không được để trống địa chỉ")
    private String provinceId;
    private String provinceName;
}
