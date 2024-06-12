package com.example.webBadminton.repository;

import com.example.webBadminton.model.Badminton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository

public interface IBadmintonRepository extends JpaRepository<Badminton, Long> {
}
