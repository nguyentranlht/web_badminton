package com.example.webBadminton.model;

import lombok.*;

import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "court")
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên sân không được để trống")
    @Column(name = "courtName")
    private String courtName;


    @ManyToOne
    @JoinColumn(name = "badminton_id", referencedColumnName = "id")
    private Badminton badminton;
}
