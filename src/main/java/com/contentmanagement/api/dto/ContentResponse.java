package com.contentmanagement.api.dto;

import com.contentmanagement.api.model.Content;
import com.contentmanagement.api.model.ContentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentResponse {

    private Long id;
    private String title;
    private String body;
    private ContentStatus status;
    private String authorUsername;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ContentResponse fromEntity(Content content) {
        return ContentResponse.builder()
                .id(content.getId())
                .title(content.getTitle())
                .body(content.getBody())
                .status(content.getStatus())
                .authorUsername(content.getAuthorUsername())
                .tags(content.getTags())
                .createdAt(content.getCreatedAt())
                .updatedAt(content.getUpdatedAt())
                .build();
    }
}