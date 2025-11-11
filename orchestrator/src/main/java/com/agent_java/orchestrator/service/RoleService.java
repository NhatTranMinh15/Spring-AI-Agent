package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.entity.Role;
import com.agent_java.orchestrator.exception.ResourceNotFoundException;
import com.agent_java.orchestrator.mapper.RoleMapper;
import com.agent_java.orchestrator.repository.RoleRepository;
import com.agent_java.orchestrator.repository.UserRepository;
import com.agent_java.orchestrator.viewmodel.RoleRequestVm;
import com.agent_java.orchestrator.viewmodel.RoleResponseVm;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private RoleMapper mapper;

    public List<RoleResponseVm> listRoles() {
        return roleRepository.findAll().stream().map(mapper::toResponseVm).toList();
    }

    @Transactional
    public RoleResponseVm createRole(RoleRequestVm request) {
        if (!roleRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Role with name '${request.getName()}' already exists");
        }
        var saved = roleRepository.save(new Role(request.getName(), request.getDescription()));
        return mapper.toResponseVm(saved);
    }

    @Transactional
    public RoleResponseVm updateRole(UUID roleId, RoleRequestVm request) {
        var role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role with id $roleId not found"));
        if (!roleRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Role with name '${request.getName()}' already exists");
        }
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role = roleRepository.save(role);
        return mapper.toResponseVm(role);
    }

    @Transactional
    public void deleteRole(UUID roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role with id $roleId not found");
        }
        roleRepository.deleteById(roleId);
    }

    public void assignRolesToUser(String username, List<String> roleNames) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User with username $username not found"));
        var roles = roleRepository.findByNameIn(roleNames);
        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("No roles found for given names: $roleNames");
        }
        user.getUserRoles().addAll(roles);
        userRepository.save(user);
    }
}
