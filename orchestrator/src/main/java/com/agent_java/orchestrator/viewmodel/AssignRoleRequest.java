package com.agent_java.orchestrator.viewmodel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class AssignRoleRequest {

    @NotNull
    private UUID id;

    @NotEmpty
    private List<@NotBlank String> roleNames;
}
