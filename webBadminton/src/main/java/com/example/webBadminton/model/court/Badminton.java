package com.example.webBadminton.model.court;

import com.example.webBadminton.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "badminton")
public class Badminton {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên sân không được để trống")
    private String badmintonName;

    @NotNull(message = "Giá thuê sân không được để trống")
    @Min(value = 1, message = "Giá thuê sân không được nhỏ hơn 1")
    private double rentalPrice;

    private double rating;  // Removed validation since it wasn't specified correctly

    @NotNull(message = "Số lượng sân không được để trống")
    @Min(value = 1, message = "Số lượng sân không được nhỏ hơn 1")
    private int courtQuantity;

    private String imageUrl;
    private String amenities;

    @NotNull(message = "Thời gian mở cửa không được để trống")
    private LocalTime openingTime;

    @NotNull(message = "Thời gian đóng cửa không được để trống")
    private LocalTime closingTime;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;  // Assuming Location is an entity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;  // Assuming User is an entity
}

