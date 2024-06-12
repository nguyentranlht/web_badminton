package com.example.webBadminton.repository;

import com.example.webBadminton.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ICourtRepository extends JpaRepository<Court, Long> {
}
