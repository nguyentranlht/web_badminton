package com.example.webBadminton.model.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Province {
    @Id
    private String id;

    private String name;

    @JsonProperty("Districts")
    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<District> districts;

    // Helper method to set province for each district
    public void setDistricts(Set<District> districts) {
        this.districts = districts;
        if (districts != null) {
            for (District district : districts) {
                district.setProvince(this);
            }
        }
    }

    // Getters and setters
}


