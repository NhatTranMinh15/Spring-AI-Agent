package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.entity.ChatMessageEntity;
import com.agent_java.orchestrator.entity.ChatMessageMediaEntity;
import com.agent_java.orchestrator.entity.ConversationEntity;
import com.agent_java.orchestrator.exception.BadRequestException;
import com.agent_java.orchestrator.exception.ResourceNotFoundException;
import com.agent_java.orchestrator.mapper.ChatMessageMapper;
import com.agent_java.orchestrator.repository.ChatMessageMediaRepository;
import com.agent_java.orchestrator.repository.ChatMessageRepository;
import com.agent_java.orchestrator.repository.ConversationRepository;
import com.agent_java.orchestrator.utils.Constant;
import com.agent_java.orchestrator.viewmodel.ChatMessageResponseVm;
import com.agent_java.orchestrator.viewmodel.ChatRequestVm;
import com.agent_java.orchestrator.viewmodel.ChatResponseVm;
import com.agent_java.orchestrator.viewmodel.ConversationResponseVm;
import com.agent_java.orchestrator.viewmodel.ConversationResponseVmImpl;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {

    private final ChatModelService chatModelService;
    private final ConversationRepository conversationRepo;
    private final ChatMessageRepository messageRepo;
    private final ChatMessageMediaRepository messageMediaRepo;

    @Autowired
    public ConversationService(ChatModelService chatModelService, ConversationRepository conversationRepo, ChatMessageRepository messageRepo, ChatMessageMediaRepository messageMediaRepo) {
        this.chatModelService = chatModelService;
        this.conversationRepo = conversationRepo;
        this.messageRepo = messageRepo;
        this.messageMediaRepo = messageMediaRepo;
    }

    @Transactional
    public ChatResponseVm createConversation(ChatRequestVm chatReq, String username) {
        if (chatReq.getConversationId() != null) {
            return addMessage(chatReq.getConversationId(), chatReq);
        }
        var conversationId = this.createNewConversation(chatReq, username);
        return addMessage(conversationId, chatReq);
    }

    public List<ConversationResponseVm> listConversationByUser(String username) {
        var conversations = conversationRepo.listActiveConversationsByUser(username);
        return conversations;
    }

    public List<ChatMessageResponseVm> listMessageByConversation(UUID conversationId) {
        conversationRepo.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found: " + conversationId));
        return messageRepo.listMessageByConversationId(conversationId).stream().map((it) -> ChatMessageMapper.toResponse(it)).toList();
    }

    private UUID createNewConversation(
            ChatRequestVm chatReq,
            String username
    ) {
        var titleSummarize = chatModelService.createSummarize(chatReq.getAgentId(), chatReq.getQuestion());
        var conversation = new ConversationEntity(
                titleSummarize != null ? titleSummarize : chatReq.getQuestion(),
                username
        );
        UUID conversationId = conversationRepo.save(conversation).getId();
        // Kotlin Elvis operator﻿
        if (conversationId == null) {
            throw new BadRequestException("Can't create new conversation");
        }
        return conversationId;
    }

    private ChatResponseVm addMessage(UUID conversationId, ChatRequestVm request) {
        var conversation = conversationRepo.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found: " + conversationId));
        // Save question into DB first
        var questionMsg = new ChatMessageEntity(request.getQuestion(), conversation, Constant.QUESTION_TYPE);
        var questionEntity = this.messageRepo.save(questionMsg);
        saveMessageMedia(request, questionEntity);
        var conversationResponse = new ConversationResponseVmImpl(conversationId, conversation.getTitle(), conversation.getCreatedAt());

        var history = messageRepo.listMessageByConversationId(conversationId).stream().map((it) -> ChatMessageMapper.toHistoryFormat(it)).toList();

        String answer = chatModelService.call(request, history);
        // Only save reply if it has actual reply
        if (answer != null) {
            var answerMsg = new ChatMessageEntity(answer, conversation, Constant.ANSWER_TYPE);
            var answerMsgEntity = messageRepo.save(answerMsg);
            return new ChatResponseVm(
                    conversationResponse,
                    new ChatMessageResponseVm(
                            answerMsgEntity.getId() != null ? answerMsgEntity.getId() : UUID.randomUUID(),
                            answerMsgEntity.getContent(),
                            answerMsgEntity.getCreatedAt(),
                            Constant.ANSWER_TYPE,
                            new ArrayList<>()
                    )
            );
        }
        return new ChatResponseVm(conversationResponse, null);
    }

    @Transactional
    public void deleteConversation(UUID conversationId) {
        var conversation = conversationRepo.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found: " + conversationId));
        conversation.setActive(false);
        conversationRepo.save(conversation);
    }

    @Transactional
    public ConversationResponseVm updateConversationTitle(
            UUID conversationId,
            String newTitle,
            String username
    ) {
        var conversation = conversationRepo.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found: " + conversationId));

        if (!conversation.getUsername().equals(username)) {
            // Do not reveal existence of the conversation to unauthorized users
            throw new ResourceNotFoundException("Conversation not found: " + conversationId);
        }

        conversation.setTitle(newTitle.trim());
        var updatedConversation = this.conversationRepo.save(conversation);

        return new ConversationResponseVmImpl(
                updatedConversation.getId(),
                updatedConversation.getTitle(),
                updatedConversation.getCreatedAt()
        );
    }

    private void saveMessageMedia(ChatRequestVm chatReq, ChatMessageEntity questionEntity) {
        var files = chatReq.getFiles();
        if (files == null || files.isEmpty()) {
            return;
        }
        List<ChatMessageMediaEntity> mediaEntities
                = files.stream().map((it) -> {
                    try {
                        return new ChatMessageMediaEntity(
                                it.getOriginalFilename() != null ? it.getOriginalFilename() : it.getName(),
                                it.getContentType() != null ? it.getContentType() : Constant.PNG_CONTENT_TYPE,
                                questionEntity,
                                it.getBytes(),
                                it.getSize());
                    } catch (IOException ex) {
                        Logger.getLogger(ConversationService.class.getName()).log(Level.SEVERE, null, ex.getMessage());
                        return null;
                    }
                }).filter((t) -> t != null).toList();
        messageMediaRepo.saveAll(mediaEntities);
    }
}
