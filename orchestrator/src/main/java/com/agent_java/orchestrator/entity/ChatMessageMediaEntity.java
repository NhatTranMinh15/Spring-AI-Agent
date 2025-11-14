package com.agent_java.orchestrator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "chat_message_media")
@Data
@AllArgsConstructor
public class ChatMessageMediaEntity extends BaseEntity {

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "content_type")
    private String contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id", nullable = false)
    private ChatMessageEntity chatMessage;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "data", columnDefinition = "bytea")
    private byte[] data;

    @Column(name = "file_size")
    private long fileSize;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }
        var e = (ChatMessageMediaEntity) other;
        if (!fileName.equals(e.fileName)) {
            return false;
        }
        if (!contentType.equals(e.contentType)) {
            return false;
        }
        return chatMessage.id == e.chatMessage.id;
    }

    @Override
    public int hashCode() {
        var result = fileName.hashCode();
        result = 31 * result + contentType.hashCode();
        result = 31 * result + chatMessage.id.hashCode();
        return result;
    }

}
