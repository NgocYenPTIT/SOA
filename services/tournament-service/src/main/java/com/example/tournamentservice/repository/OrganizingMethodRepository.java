package com.example.tournamentservice.repository;

import com.example.tournamentservice.model.OrganizingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizingMethodRepository extends JpaRepository<OrganizingMethod, Long> {
}