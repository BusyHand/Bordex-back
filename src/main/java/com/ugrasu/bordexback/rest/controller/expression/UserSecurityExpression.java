package com.ugrasu.bordexback.rest.controller.expression;

import com.ugrasu.bordexback.rest.controller.filter.BoardRolesFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("use")
@RequiredArgsConstructor
public class UserSecurityExpression extends SecurityExpressionPrincipal {

    public boolean isTheSameUser(Long userId) {
        if (userId == null) {
            return false;
        }
        return getPrincipal().getUserId().equals(userId);
    }

    public boolean isTheSameUser(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        Long authUserId = getPrincipal().getUserId();
        return userIds.stream()
                .allMatch(authUserId::equals);
    }

    public boolean chek(BoardRolesFilter filter){
        return true;
    }

}
