package com.agent_java.orchestrator.entity;

import com.agent_java.orchestrator.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "system_setting")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingEntity extends BaseEntity {

    @Column(name = "site_name")
    String siteName;
    
    @Column(name = "maximum_user")
    int maximumUser;
    
    @Column(name = "session_timeout")
    int sessionTimeout;
    
    @Column(name = "maximum_size_file_upload")
    int maximumSizeFileUpload;
    
    @Column(name = "allowed_file_types")
    String allowedFileTypes;
    
    @Column(name = "maintenance_mode")
    boolean maintenanceMode;
    
    @Column(name = "user_registration")
    boolean userRegistration;
    
    @Column(name = "email_verification")
    boolean emailVerification;
}
