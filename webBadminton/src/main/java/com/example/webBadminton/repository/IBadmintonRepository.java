package com.example.webBadminton.repository;

import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.model.court.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface IBadmintonRepository extends JpaRepository<Badminton, Long> {

    @Query("SELECT b FROM Badminton b WHERE b.location IN :locations")
    List<Badminton> findByLocations(@Param("locations") List<Location> locations);

    @Query("SELECT b FROM Badminton b WHERE b.user.id = ?1")
    List<Badminton> findByUser(Long userId);
}
