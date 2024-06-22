package com.example.webBadminton.repository;

import com.example.webBadminton.model.court.Badminton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface IBadmintonRepository extends JpaRepository<Badminton, Long> {

//    List<Badminton> findByLocationAndTime(String province, String district, String ward, LocalDate day, LocalTime startTime, LocalTime endTime);
}
