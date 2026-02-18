package com.onlinevoting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ElectionResultDTO {

    private Long candidateId;
    private String candidateName;
    private String firstName;
    private String lastName;
    private String middleName;
    private Long votes;
    private String partyName;
    private Boolean isWinner;
    private Double percentage;
    private String candidateImageUrl;
    private String partyImageUrl;
}
