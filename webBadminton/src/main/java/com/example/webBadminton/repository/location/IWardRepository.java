package com.example.webBadminton.repository.location;

import com.example.webBadminton.model.location.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWardRepository extends JpaRepository<Ward, String> {
}
