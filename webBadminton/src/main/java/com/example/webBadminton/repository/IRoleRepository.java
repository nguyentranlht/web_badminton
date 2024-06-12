package com.example.webBadminton.repository;

import com.example.webBadminton.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IRoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r.id FROM Role r WHERE r.name = :roleName")
    Long getRoleIdByName(String roleName);
}
