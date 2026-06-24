package com.contentmanagement.api.repository;

import com.contentmanagement.api.model.Content;
import com.contentmanagement.api.model.ContentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {

    Page<Content> findByStatus(ContentStatus status, Pageable pageable);

    Page<Content> findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(
            String titleKeyword,
            String bodyKeyword,
            Pageable pageable
    );
}