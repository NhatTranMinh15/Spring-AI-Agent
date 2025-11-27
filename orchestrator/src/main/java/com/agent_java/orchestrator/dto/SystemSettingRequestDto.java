package com.agent_java.orchestrator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingRequestDto {

    @NotBlank
    String siteName;

    @NotNull
    int maximumUser;

    @NotNull
    int sessionTimeout;

    @NotNull
    int maximumSizeFileUpload;

    @NotBlank
    String allowedFileTypes;

    @NotNull
    boolean maintenanceMode;

    @NotNull
    boolean userRegistration;

    @NotNull
    boolean emailVerification;
}
