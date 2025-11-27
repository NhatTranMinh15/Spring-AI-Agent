package com.agent_java.orchestrator.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemSettingResponseDto {

    UUID id;

    String siteName;

    int maximumUser;

    int sessionTimeout;

    int maximumSizeFileUpload;

    String allowedFileTypes;

    boolean maintenanceMode;

    boolean userRegistration;

    boolean emailVerification;
}
