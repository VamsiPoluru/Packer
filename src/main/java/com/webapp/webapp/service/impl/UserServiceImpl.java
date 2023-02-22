package com.webapp.webapp.service.impl;

import java.util.Date;

import javax.persistence.AttributeOverride;

import org.hibernate.annotations.FetchProfile.FetchOverride;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.webapp.webapp.service.UserService;
import com.webapp.webapp.entity.User;
import com.webapp.webapp.exception.BusinessException;
import com.webapp.webapp.dto.UserDTO;
import com.webapp.webapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.webapp.webapp.model.request.UserDetailsRequestModel;
import com.webapp.webapp.model.ui.UserRest;
import com.webapp.webapp.utils.ErrorMessages;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    BCryptPasswordEncoder bcryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bcryptPasswordEncoder) {
        super();
        this.userRepository = userRepository;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }

    @Override
    public UserRest createUser(UserDTO userdto) {
        try {
            User userEmail = userRepository.findByEmail(userdto.getEmail());
            if (userEmail != null) {
                throw new BusinessException("601", "Email Address already in use.");
            }
        } catch (Exception e) {
            throw new BusinessException("400", "Email Address already in use.");
        }

        User user = new User();
        String encodedpass = bcryptPasswordEncoder.encode(userdto.getPassword());
        BeanUtils.copyProperties(userdto, user);
        user.setEncryptedPassword(encodedpass);
        User storedUser = userRepository.save(user);

        UserRest returnUser = new UserRest();
        BeanUtils.copyProperties(storedUser, returnUser);
        return returnUser;
    }

    @Override
    public UserRest findUser(long id) {
        try {
            User user = new User();
            UserRest returnUser = new UserRest();
            user = userRepository.findById(id);
            BeanUtils.copyProperties(user, returnUser);
            return returnUser;

        } catch (Exception e) {
            throw new BusinessException("403", "User ID not present.");
        }

    }

    @Override
    public UserRest findUserByMailId(UserDTO userDTO) {
        try {
            User user = userRepository.findByEmail(userDTO.getEmail());
            if (user == null) {
                throw new BusinessException("404", "User not found.");
            }
            UserRest returnUser = new UserRest();
            BeanUtils.copyProperties(user, returnUser);
            return returnUser;
        } catch (Exception e) {
            throw new BusinessException("404", "User not found.");
        }
    }

    @Override
    public UserRest updateUser(UserDTO userdto) {
        User user = userRepository.findByEmail(userdto.getEmail());
        String encodedpass = bcryptPasswordEncoder.encode(userdto.getPassword());
        user.setEncryptedPassword(encodedpass);
        user.setFirstName(userdto.getFirstName());
        user.setLastName(userdto.getLastName());
        user.setAccount_updated(new Date(System.currentTimeMillis()));
        User storedUser = userRepository.save(user);
        UserRest returnUser = new UserRest();
        BeanUtils.copyProperties(storedUser, returnUser);
        return returnUser;
    }

    // @Override
    // public UserRest authenticateUser(UserDTO userdto) {
    // User user = userRepository.findByEmail(userdto.getEmail());
    // String encodedpass = bcryptPasswordEncoder.encode(userdto.getPassword());
    // if (user != null && encodedpass == user.getEncryptedPassword()) {
    // UserRest returnUser = new UserRest();
    // BeanUtils.copyProperties(user, returnUser);
    // return returnUser;
    // }
    // return null;
    // }

    @Override
    public User authenticateUser(long id) {
        try {
            User user = new User();
            UserRest returnUser = new UserRest();
            user = userRepository.findById(id);
            return user;
        } catch (Exception e) {
            throw new BusinessException("400", "User not found.");
        }

    }
}
