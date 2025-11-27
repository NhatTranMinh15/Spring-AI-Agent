package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.dto.SystemSettingRequestDto;
import com.agent_java.orchestrator.dto.SystemSettingResponseDto;
import com.agent_java.orchestrator.service.SystemSettingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class SystemSettingController {

    private final SystemSettingService systemSettingService;

    @Autowired
    public SystemSettingController(SystemSettingService systemSettingService) {
        this.systemSettingService = systemSettingService;
    }

    @GetMapping
    public ResponseEntity<SystemSettingResponseDto> getSystemSetting() {
        return ResponseEntity.ok(systemSettingService.getSystemSetting());
    }

    @PutMapping
    public ResponseEntity<SystemSettingResponseDto> updateSystemSetting(@Valid @RequestBody SystemSettingRequestDto request) {
        return ResponseEntity.ok(systemSettingService.updateSystemSetting(request));
    }
}
