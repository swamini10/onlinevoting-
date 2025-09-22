package com.onlinevoting.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.onlinevoting.dto.StateDTO;
import com.onlinevoting.model.State;
import com.onlinevoting.repository.StateRepository;

@Service
public class StateService {

    private final StateRepository  stateRepository;

    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public List<StateDTO> getAll() {
        List<State> list = stateRepository.findAll();
        List<StateDTO> dtoList = new ArrayList<>();
        
        for (State state : list) {
            StateDTO dto = new StateDTO(state.getId(), state.getName(), 
            state.geCountry().getId());
            dtoList.add(dto);
        }

        return dtoList;
    }
}
