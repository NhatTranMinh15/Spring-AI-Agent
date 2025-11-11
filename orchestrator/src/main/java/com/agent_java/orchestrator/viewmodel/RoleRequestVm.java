package com.agent_java.orchestrator.viewmodel;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleRequestVm {

    @NotBlank
    private String name;
    private String description;
    
    
}
