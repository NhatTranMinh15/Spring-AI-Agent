package com.agent_java.authorization_server.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPageDto {

    List<UserDto> users;
    int pageNumber;
    int pageSize;
    long totalElements;
    int totalPages;
    boolean lastPage;

    public UserPageDto() {
    }
}
