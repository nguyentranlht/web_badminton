package com.example.webBadminton.repository;

import com.example.webBadminton.model.court.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILocationRepository extends JpaRepository<Location, Long> {
    // Find by Province Code
    @Query("SELECT l FROM Location l WHERE" +
            "(:provinceId IS NULL OR l.provinceId = :provinceId)")
    List<Location> findByProvinceId(String provinceId);

    // Find by District Code
    @Query("SELECT l FROM Location l WHERE" +
            "(:districtId IS NULL OR l.districtId = :districtId)")
    List<Location> findByDistrictId(String districtId);

    // Find by Ward Code
    @Query("SELECT l FROM Location l WHERE" +
            "(:wardId IS NULL OR l.wardId = :wardId)")
    List<Location> findByWardId(String wardId);

    // Advanced query: find by any combination of province, district, or ward code
    @Query("SELECT l FROM Location l WHERE " +
            "(:provinceId IS NULL OR l.provinceId = :provinceId) AND " +
            "(:districtId IS NULL OR l.districtId = :districtId) AND " +
            "(:wardId IS NULL OR l.wardId = :wardId)")
    List<Location> findLocations(
            @Param("provinceId") String provinceId,
            @Param("districtId") String districtId,
            @Param("wardId") String wardId);
}
