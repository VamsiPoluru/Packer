package com.webapp.webapp.dto;

import lombok.Data;
import java.util.Date;

@Data
public class UserDTO {

    private static final long serialVersionUID = 1L;
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
