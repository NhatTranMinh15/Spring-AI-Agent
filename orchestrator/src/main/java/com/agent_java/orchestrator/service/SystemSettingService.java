package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.dto.SystemSettingRequestDto;
import com.agent_java.orchestrator.dto.SystemSettingResponseDto;
import com.agent_java.orchestrator.entity.SystemSettingEntity;
import com.agent_java.orchestrator.exception.ResourceNotFoundException;
import com.agent_java.orchestrator.repository.SystemSettingRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemSettingService {

    private SystemSettingRepository systemSetting;

    private static final UUID settingId = UUID.fromString("67078936-185a-43f7-b36c-03b3e7aff4f0");

    @Autowired
    public SystemSettingService(SystemSettingRepository systemSetting) {
        this.systemSetting = systemSetting;
    }

    @Transactional(readOnly = true)
    public SystemSettingResponseDto getSystemSetting() {
        var settings = systemSetting.findById(settingId).orElseThrow(() -> new ResourceNotFoundException("No system setting found"));
        return toSystemSettingResponseDto(settings);
    }

    @Transactional
    public SystemSettingResponseDto updateSystemSetting(SystemSettingRequestDto request) {
        var setting = systemSetting.findById(settingId).orElseThrow(() -> new ResourceNotFoundException("No system setting found"));

        setting.setSiteName(request.getSiteName());
        setting.setMaximumUser(request.getMaximumUser());
        setting.setSessionTimeout(request.getSessionTimeout());
        setting.setMaximumSizeFileUpload(request.getMaximumSizeFileUpload());
        setting.setAllowedFileTypes(request.getAllowedFileTypes());
        setting.setMaintenanceMode(request.isMaintenanceMode());
        setting.setUserRegistration(request.isUserRegistration());
        setting.setEmailVerification(request.isEmailVerification());

        setting = systemSetting.save(setting);
        return toSystemSettingResponseDto(setting);
    }

    private static SystemSettingResponseDto toSystemSettingResponseDto(SystemSettingEntity settings) {
        return new SystemSettingResponseDto(
                settings.getId(),
                settings.getSiteName(),
                settings.getMaximumUser(),
                settings.getSessionTimeout(),
                settings.getMaximumSizeFileUpload(),
                settings.getAllowedFileTypes(),
                settings.isMaintenanceMode(),
                settings.isUserRegistration(),
                settings.isEmailVerification()
        );
    }
}
