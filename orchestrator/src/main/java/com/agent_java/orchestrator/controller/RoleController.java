package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.service.RoleService;
import com.agent_java.orchestrator.viewmodel.AssignRoleRequest;
import com.agent_java.orchestrator.viewmodel.RoleRequestVm;
import com.agent_java.orchestrator.viewmodel.RoleResponseVm;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    RoleService roleService;

    @GetMapping
    ResponseEntity<List<RoleResponseVm>> listRoles() {
        return ResponseEntity.ok(roleService.listRoles());
    }

    ResponseEntity<RoleResponseVm> createRole(@Valid @RequestBody RoleRequestVm request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @PutMapping("/{roleId}")
    ResponseEntity<RoleResponseVm> updateRole(@PathVariable UUID roleId, @Valid @RequestBody RoleRequestVm request) {
        return ResponseEntity.ok(roleService.updateRole(roleId, request));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity deleteRole(@PathVariable UUID roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assignRoles(@Valid @RequestBody AssignRoleRequest request) {
        roleService.assignRolesToUser(request.getUsername(), request.getRoleNames());
        return ResponseEntity.noContent().build();
    }
}
