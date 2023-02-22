package com.webapp.webapp.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.BeanUtils;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.lang.CharSequence;

import com.webapp.webapp.entity.User;
import com.webapp.webapp.exception.BusinessException;
import com.webapp.webapp.exception.ControllerException;
import com.webapp.webapp.dto.UserAuth;
import com.webapp.webapp.dto.UserDTO;
import com.webapp.webapp.model.ui.UserRest;
import com.webapp.webapp.repository.UserRepository;
import com.webapp.webapp.service.UserService;
import com.webapp.webapp.model.request.UserDetailsRequestModel;
import com.webapp.webapp.utils.ErrorMessages;

@RestController
public class Controller {

    private UserService userService;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bcryptPasswordEncoder;

    public Controller(UserService userService, BCryptPasswordEncoder bcryptPasswordEncoder) {
        super();
        this.userService = userService;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;

    }

    @GetMapping("/healthz")
    public ResponseEntity<?> healthz() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // build create user
    @PostMapping(path = "/v1/account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveUser(@RequestBody UserDetailsRequestModel userDetails) {

        try {
            if (userDetails.getEmail().isEmpty() || userDetails.getFirstName().isEmpty()
                    || userDetails.getLastName().isEmpty() || userDetails.getPassword().isEmpty()) {
                throw new ControllerException("400", "Please fill in all the details.");
            }
            UserRest userRest = new UserRest();
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userDetails, userDTO);

            userRest = userService.createUser(userDTO);
            return new ResponseEntity<>(userRest, HttpStatus.CREATED);

        } catch (BusinessException be) {
            ControllerException ce = new ControllerException(be.getErrorCode(), be.getErrorMessage());
            return new ResponseEntity<>(ce, HttpStatus.BAD_REQUEST);
        } catch (ControllerException ee) {
            return new ResponseEntity<>(ee, HttpStatus.BAD_REQUEST);
        }
    }

    // get user
    @GetMapping(path = "/v1/account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable Long id, HttpServletRequest request) {
        try {
            String usernamepass = authenticateUser(request);
            String userName = usernamepass.split(":")[0];
            String passWord = usernamepass.split(":")[1];
            CharSequence cs = passWord;
            User user = userService.authenticateUser(id);
            if (userName == null
                    || passWord == null) {
                throw new ControllerException("401", "Unauthorized");
            }
            if (userName.equals(user.getEmail())
                    && user.getId() == id
                    && bcryptPasswordEncoder.matches(cs, user.getEncryptedPassword())) {
                try {
                    if (id == null) {
                        throw new ControllerException("403", "Bad url");
                    }
                    UserRest userRest = new UserRest();
                    userRest = userService.findUser(id);
                    return new ResponseEntity<>(userRest, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new ControllerException("403", "Forbidden");
            }

        } catch (ControllerException ee) {
            if (ee.getErrorCode().equals("403")) {
                return new ResponseEntity<>(ee, HttpStatus.FORBIDDEN);
            } else if (ee.getErrorCode().equals("401")) {
                return new ResponseEntity<>(ee, HttpStatus.UNAUTHORIZED);
            }

        } catch (BusinessException be) {
            ControllerException ce = new ControllerException(be.getErrorCode(),
                    be.getErrorMessage());
            return new ResponseEntity<>(ce, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            ControllerException ce = new ControllerException("401", "Unauthorized");
            return new ResponseEntity<>(ce, HttpStatus.UNAUTHORIZED);
        }
        ControllerException ce = new ControllerException("401", "Unauthorized");
        return new ResponseEntity<>(ce, HttpStatus.UNAUTHORIZED);
    }

    // putuser
    @PutMapping(path = "/v1/account/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> UpdateUser(@RequestBody UserDetailsRequestModel userDetails, @PathVariable long id,
            HttpServletRequest request) {
        try {
            String usernamepass = authenticateUser(request);
            String userName = usernamepass.split(":")[0];
            String passWord = usernamepass.split(":")[1];
            CharSequence cs = passWord;
            User user = userService.authenticateUser(id);
            if (userName == null
                    || passWord == null) {
                throw new ControllerException("401", "Unauthorized");
            }
            if (userName.equals(user.getEmail())
                    && user.getId() == id
                    && bcryptPasswordEncoder.matches(cs, user.getEncryptedPassword())) {

                try {
                    if (userDetails.getEmail().isEmpty() || userDetails.getFirstName().isEmpty()
                            || userDetails.getLastName().isEmpty() || userDetails.getPassword().isEmpty()) {
                        throw new ControllerException("400", "Please fill in all the details.");
                    }

                    UserDTO userDTO = new UserDTO();
                    UserRest userRest = new UserRest();
                    BeanUtils.copyProperties(userDetails, userDTO);
                    userRest = userService.findUserByMailId(userDTO);
                    if (id == userRest.getId()) {
                        UserDTO userDTO1 = new UserDTO();
                        UserRest userRest1 = new UserRest();
                        BeanUtils.copyProperties(userDetails, userDTO1);
                        userRest = userService.updateUser(userDTO1);
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    } else {
                        throw new ControllerException("403", "Access Denied");
                    }

                } catch (ControllerException ee) {
                    if (ee.getErrorCode().equals("403")) {
                        return new ResponseEntity<>(ee, HttpStatus.FORBIDDEN);
                    } else {
                        return new ResponseEntity<>(ee, HttpStatus.BAD_REQUEST);
                    }

                } catch (BusinessException be) {
                    ControllerException ce = new ControllerException(be.getErrorCode(), be.getErrorMessage());
                    return new ResponseEntity<>(ce, HttpStatus.NOT_FOUND);
                }
            } else {
                throw new ControllerException("403", "Forbidden");
            }
        } catch (ControllerException ee) {
            if (ee.getErrorCode().equals("403")) {
                return new ResponseEntity<>(ee, HttpStatus.FORBIDDEN);
            } else if (ee.getErrorCode().equals("401")) {
                return new ResponseEntity<>(ee, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            ControllerException ce = new ControllerException("400", "Bad Request");
            return new ResponseEntity<>(ce, HttpStatus.BAD_REQUEST);
        }

        ControllerException ce = new ControllerException("401", "Unauthorized");
        return new ResponseEntity<>(ce, HttpStatus.UNAUTHORIZED);

    }

    private String authenticateUser(HttpServletRequest request) {

        try {
            String tokenEnc = request.getHeader("Authorization").split(" ")[1];
            byte[] token = Base64.getDecoder().decode(tokenEnc);
            String decodedStr = new String(token, StandardCharsets.UTF_8);

            String userName = decodedStr.split(":")[0];
            String passWord = decodedStr.split(":")[1];

            return decodedStr;

        } catch (Exception e) {
            throw new ControllerException("401", "Unauthorized");
        }
    }
}
