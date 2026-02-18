package com.onlinevoting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VotingDTO {

    private Long voterId;
    private Long electionId;
    private Long candidateId;
}
