package com.fiuni.adoptamena.api.dto.user;

import java.util.Date;

import com.fiuni.adoptamena.api.dto.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserProfileDTO extends BaseDTO {
    private String fullName;
    private String email;
    private Date creationDate;
    private String role;
    private Boolean isVerified;
}
