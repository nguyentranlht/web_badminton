package com.example.webBadminton.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;

@Data
@Entity
@Table(name = "role")
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must be less than 50 characters")
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Size(max = 250, message = "Description must be less than 250 characters")
    @Column(name = "description", length = 250)
    private String description;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

    public Role(){}
}
