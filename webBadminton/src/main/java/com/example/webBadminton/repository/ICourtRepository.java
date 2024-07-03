package com.example.webBadminton.repository;

import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.model.court.CourtId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICourtRepository extends JpaRepository<Court, CourtId> {

    @Query("SELECT c FROM Court c WHERE c.badmintonId = ?1")
    List<Court> findByBadmintonId(Long id);


    @Query("SELECT c FROM Court c WHERE c.badmintonId IN :badmintonIds")
    List<Court> findAllByBadmintonIds(@Param("badmintonIds") List<Long> badmintonIds);
}
