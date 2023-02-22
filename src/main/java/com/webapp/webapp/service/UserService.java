package com.webapp.webapp.service;

import com.webapp.webapp.dto.UserDTO;
import com.webapp.webapp.entity.User;
import com.webapp.webapp.model.ui.UserRest;

public interface UserService {

    UserRest createUser(UserDTO userdto);

    UserRest findUser(long id);

    UserRest findUserByMailId(UserDTO userdto);

    UserRest updateUser(UserDTO userdto);

    User authenticateUser(long id);

    // UserRest authenticateUser(UserDTO userdto);
}
