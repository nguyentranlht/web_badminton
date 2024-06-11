package com.example.webBadminton.model;

import lombok.*;

import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "badminton_details")
public class BadmintonDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên sân không được để trống")
    private String courtName;

    @ManyToOne
    @JoinColumn(name = "badminton_id")
    private Badminton badminton;
}
