package com.example.task.web.security.expression;

import com.example.task.domain.user.Role;
import com.example.task.service.UserService;
import com.example.task.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component("cse")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final UserService userService;

    public boolean canAccessUserById(Long id) {
        JwtEntity user = getPrincipal();
        Long userId = user.getId();

        System.out.println(userId.equals(id) || hasAnyRole(Role.ROLE_ADMIN));

        return userId.equals(id) || hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean canAccessUserByUsername(String username) {
        JwtEntity user = getPrincipal();
        String userUsername = user.getUsername();

        System.out.println(userUsername + " : " + username);

        return userUsername.equals(username) || hasAnyRole(Role.ROLE_ADMIN);
    }

    private boolean hasAnyRole(Role... roles) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        for (Role role : roles) {
            SimpleGrantedAuthority authority
                    = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

    public boolean canAccessTask(Long taskId) {
        JwtEntity user = getPrincipal();
        Long userId = user.getId();

        return userService.isTaskOwner(userId, taskId);
    }

    private JwtEntity getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        return (JwtEntity) authentication.getPrincipal();
    }

}
