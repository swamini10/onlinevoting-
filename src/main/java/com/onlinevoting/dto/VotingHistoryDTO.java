package com.onlinevoting.dto;

import java.time.LocalDateTime;

import com.onlinevoting.enums.VotingStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VotingHistoryDTO {

    private String electionName;
    private String voterId;
    private LocalDateTime votingStartTime;
    private LocalDateTime votingEndTime;
    private VotingStatus votingStatus;
}
