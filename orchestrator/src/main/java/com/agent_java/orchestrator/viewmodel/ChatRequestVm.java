package com.agent_java.orchestrator.viewmodel;

import com.agent_java.orchestrator.validation.ValidChatFile;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@ValidChatFile
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestVm {

    @Nullable
    UUID conversationId;

    @NotBlank(message = "Question must not be blank")
    String question;

    @Nullable
    @Size(max = 3, message = "Maximum 3 files allowed")
    List<MultipartFile> files;

    @Nullable
    UUID agentId;

}
