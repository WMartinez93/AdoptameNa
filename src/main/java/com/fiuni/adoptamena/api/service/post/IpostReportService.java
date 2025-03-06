package com.fiuni.adoptamena.api.service.post;

import com.fiuni.adoptamena.api.domain.post.PostReportDomain;
import com.fiuni.adoptamena.api.dto.post.PostReportDto;
import com.fiuni.adoptamena.api.service.base.IBaseService;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IpostReportService extends IBaseService<PostReportDomain, PostReportDto> {
    List<PostReportDto> getAllPostsReports(Pageable pageable, Integer userId, Integer postId, Integer reportReasonsId, String description);
}
