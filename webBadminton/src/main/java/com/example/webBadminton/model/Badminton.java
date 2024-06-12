//package com.example.webBadminton.model;
//
//import jakarta.persistence.*;
//import javax.validation.constraints.Max;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import javax.xml.stream.Location;
//
//import lombok.*;
//import org.hibernate.validator.constraints.Length;
//
//import java.util.List;
//
//@Setter
//@Getter
//@RequiredArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "badminton")
//public class Badminton {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank(message = "Tên sân không được để trống")
//    private String name;
//
//    @NotBlank(message = "Địa chỉ không được để trống")
//    private String address;
//
//    @NotNull(message = "Giá thuê sân không được để trống")
//    @Min(value = 1, message = "Giá thuê sân không được nhỏ hơn 1")
//    private double rentalPrice;
//
//    @NotNull(message = "Số lượng sân không được để trống")
//    @Min(value = 1, message = "Số lượng sân không được nhỏ hơn 1")
//    private int courtQuantity;
//
//    //@Length(min = 0, max = 200, message = "Tên hình ảnh không quá 200 ký tự")
//    private String imageUrl;
//
//    @ManyToOne
//    @JoinColumn(name = "location_id")
//    private Location location;
//
//    @Min(value = 1, message = "Số người tối thiểu phải là 1")
//    private int minPlayers;
//
//    @Max(value = 4, message = "Số người tối đa phải là 4")
//    private int maxPlayers;
//
//    private String amenities; // Tiện nghi có sẵn tại sân
//
//    @NotBlank(message = "Thời gian mở cửa không được để trống")
//    private String openingTime;
//
//    @NotBlank(message = "Thời gian đóng cửa không được để trống")
//    private String closingTime;
//
//    @OneToMany(mappedBy = "badmintonCourt", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<BadmintonDetail> badmintonDetails;
//}
