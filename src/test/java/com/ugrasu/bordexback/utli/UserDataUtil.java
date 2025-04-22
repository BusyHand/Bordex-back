package com.ugrasu.bordexback.utli;

import com.ugrasu.bordexback.entity.User;
import com.ugrasu.bordexback.entity.enums.Role;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class UserDataUtil {

    public User getSimpleUser(){
        return User.builder()
                .username("UserName")
                .password("Password")
                .firstName("FirstName")
                .lastName("LastName")
                .email("Email")
                .roles(Set.of(Role.USER))
                .build();
    }
}
