package com.example.webBadminton.model.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class District {
    @Id
    private String id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;

    @JsonProperty("Wards")
    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ward> wards;

    // Helper method to set district for each ward
    public void setWards(Set<Ward> wards) {
        this.wards = wards;
        if (wards != null) {
            for (Ward ward : wards) {
                ward.setDistrict(this);
            }
        }
    }

    // Getters and setters
}



