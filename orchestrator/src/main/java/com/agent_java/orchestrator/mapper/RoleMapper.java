package com.agent_java.orchestrator.mapper;

import com.agent_java.orchestrator.entity.Role;
import com.agent_java.orchestrator.viewmodel.RoleResponseVm;

public class RoleMapper {

    public RoleResponseVm toResponseVm(Role role) {
        return new RoleResponseVm(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
    }
}
