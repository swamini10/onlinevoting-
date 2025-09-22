package com.onlinevoting.repository;

import org.springframework.data.repository.ListCrudRepository;

import com.onlinevoting.model.State;


public interface StateRepository extends ListCrudRepository<State, Long>{

}
