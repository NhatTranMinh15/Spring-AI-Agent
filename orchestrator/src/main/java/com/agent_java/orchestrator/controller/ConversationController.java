package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.service.ConversationService;
import com.agent_java.orchestrator.viewmodel.ChatMessageResponseVm;
import com.agent_java.orchestrator.viewmodel.ChatRequestVm;
import com.agent_java.orchestrator.viewmodel.ChatResponseVm;
import com.agent_java.orchestrator.viewmodel.ConversationResponseVm;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ChatResponseVm> createConversation(@Valid @ModelAttribute ChatRequestVm req, Authentication authentication) {
        var username = ((Jwt) authentication.getPrincipal()).getSubject();
        var conversation = conversationService.createConversation(req, username);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ConversationResponseVm>> getConversations(Authentication authentication) {
        var username = ((Jwt) authentication.getPrincipal()).getSubject();
        var conversations = conversationService.listConversationByUser(username);
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<ChatMessageResponseVm>> getMessages(@PathVariable UUID conversationId) {
        return ResponseEntity.ok(conversationService.listMessageByConversation(conversationId));
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity deleteConversation(@PathVariable UUID conversationId) {
        conversationService.deleteConversation(conversationId);
        return ResponseEntity.noContent().build();
    }
}
