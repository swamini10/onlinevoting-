package com.onlinevoting.service;

import java.util.List;

import com.onlinevoting.dto.UserDetailDTO;

public interface VoterSearchService {

    public List<UserDetailDTO> searchVoters(String searchCriteria,String searchValue);
}
