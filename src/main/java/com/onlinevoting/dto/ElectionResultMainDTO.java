package com.onlinevoting.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ElectionResultMainDTO {
 
    private String electionName;
    private String electionDate;
    private String electionResultDate;
    private List<ElectionResultDTO> electionResults;
}
