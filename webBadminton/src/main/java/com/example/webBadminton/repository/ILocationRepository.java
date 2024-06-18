package com.example.webBadminton.repository;

import com.example.webBadminton.model.Badminton;
import com.example.webBadminton.model.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILocationRepository extends JpaRepository<Location, Long> {
    // Find by Province Code
    List<Location> findByProvinceCode(String provinceCode);

    // Find by District Code
    List<Location> findByDistrictCode(String districtCode);

    // Find by Ward Code
    List<Location> findByWardCode(String wardCode);

    // Advanced query: find by any combination of province, district, or ward code
    @Query("SELECT l FROM Location l WHERE " +
            "(:provinceCode IS NULL OR l.provinceCode = :provinceCode) AND " +
            "(:districtCode IS NULL OR l.districtCode = :districtCode) AND " +
            "(:wardCode IS NULL OR l.wardCode = :wardCode)")
    List<Location> findLocations(
            @Param("provinceCode") String provinceCode,
            @Param("districtCode") String districtCode,
            @Param("wardCode") String wardCode);
}
