package com.agent_java.orchestrator.mapper;

import com.agent_java.orchestrator.entity.RoleEntity;
import com.agent_java.orchestrator.viewmodel.RoleResponseVm;
import org.springframework.stereotype.Service;

@Service
public class RoleMapper {

    public RoleResponseVm toResponseVm(RoleEntity role) {
        return new RoleResponseVm(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
    }
}
