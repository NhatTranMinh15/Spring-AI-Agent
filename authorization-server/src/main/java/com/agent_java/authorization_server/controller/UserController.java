package com.agent_java.authorization_server.controller;

import com.agent_java.authorization_server.dto.UserPageDto;
import com.agent_java.authorization_server.service.UserService;
import com.agent_java.authorization_server.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserPageDto> getUsers(
            @RequestParam(defaultValue = Constant.PAGE_NUMBER, required = false) int page,
            @RequestParam(defaultValue = Constant.PAGE_SIZE, required = false) int size
    ) {
        
        return ResponseEntity.ok(userService.getUsers(page, size));
    }
}
