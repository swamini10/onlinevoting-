package com.onlinevoting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO  {
    private String firstName;
    private String lastName;
    private String emailId;
    private String id;  
    private String status;
}