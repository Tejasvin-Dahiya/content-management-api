package com.contentmanagement.api.service;

import com.contentmanagement.api.dto.ContentRequest;
import com.contentmanagement.api.dto.ContentResponse;
import com.contentmanagement.api.exception.ResourceNotFoundException;
import com.contentmanagement.api.model.Content;
import com.contentmanagement.api.model.ContentStatus;
import com.contentmanagement.api.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    public Page<ContentResponse> getAllContent(ContentStatus status, Pageable pageable) {
        Page<Content> contentPage;
        if (status != null) {
            contentPage = contentRepository.findByStatus(status, pageable);
        } else {
            contentPage = contentRepository.findAll(pageable);
        }
        return contentPage.map(ContentResponse::fromEntity);
    }

    public ContentResponse getContentById(Long id) {
        Content content = findContentOrThrow(id);
        return ContentResponse.fromEntity(content);
    }

    @Transactional
    public ContentResponse createContent(ContentRequest request, String authorUsername) {
        Content content = Content.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .status(request.getStatus())
                .authorUsername(authorUsername)
                .tags(request.getTags() != null ? request.getTags() : new ArrayList<>())
                .build();

        Content saved = contentRepository.save(content);
        return ContentResponse.fromEntity(saved);
    }

    @Transactional
    public ContentResponse updateContent(Long id, ContentRequest request) {
        Content content = findContentOrThrow(id);

        content.setTitle(request.getTitle());
        content.setBody(request.getBody());
        content.setStatus(request.getStatus());
        if (request.getTags() != null) {
            content.setTags(request.getTags());
        }

        Content updated = contentRepository.save(content);
        return ContentResponse.fromEntity(updated);
    }

    @Transactional
    public void deleteContent(Long id) {
        Content content = findContentOrThrow(id);
        contentRepository.delete(content);
    }

    public Page<ContentResponse> searchContent(String keyword, Pageable pageable) {
        Page<Content> results = contentRepository
                .findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(keyword, keyword, pageable);
        return results.map(ContentResponse::fromEntity);
    }

    private Content findContentOrThrow(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found with id: " + id));
    }
}