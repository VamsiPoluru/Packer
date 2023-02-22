package com.webapp.webapp.model.ui;

import lombok.Data;

import java.util.Date;

@Data
public class UserRest {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Date account_created;
    private Date account_updated;
}
