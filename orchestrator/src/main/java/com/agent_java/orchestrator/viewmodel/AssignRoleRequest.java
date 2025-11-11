package com.agent_java.orchestrator.viewmodel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class AssignRoleRequest {

    @NotBlank
    private String username;

    @NotEmpty
    private List<@NotBlank String> roleNames;
}
