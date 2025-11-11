package com.agent_java.orchestrator.viewmodel;

import jakarta.annotation.Nullable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleResponseVm {

    private UUID id;
    private String name;
    @Nullable
    private String description;

}
