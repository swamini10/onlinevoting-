package com.onlinevoting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class ElectionDataPoint {

    private Long totalElections;
    private Long totalElectionApproved;
    private Long totalElectionResultPublished;
    private Long totalElectionResultUnPublished;
    private Long totalElectionunApproved;
}
