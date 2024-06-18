package com.example.webBadminton.model.location;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Ward {
    @Id
    private String id;

    private String name;

    @JsonProperty("Level")
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;



    // Getters and setters
}
