package com.example.webBadminton.repository;

import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.model.court.CourtId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ICourtRepository extends JpaRepository<Court, CourtId> {

}
