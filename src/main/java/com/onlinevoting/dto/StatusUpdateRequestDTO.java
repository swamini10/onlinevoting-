package com.onlinevoting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequestDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;
    private String dateOfBirth;
    private String aadharNumber;
    private String status;
    private String note;
    private Boolean isPublish;

    
}
