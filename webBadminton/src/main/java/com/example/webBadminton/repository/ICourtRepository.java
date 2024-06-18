package com.example.webBadminton.repository;

import com.example.webBadminton.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICourtRepository extends JpaRepository<Court, Long> {
    List<Court> findByBadmintonId(Long badmintonId);
}
