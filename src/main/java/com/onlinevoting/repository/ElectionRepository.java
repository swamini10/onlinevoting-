package com.onlinevoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onlinevoting.model.Election;

@Repository
public interface ElectionRepository extends JpaRepository<Election, Long> {

}
