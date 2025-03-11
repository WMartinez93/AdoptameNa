package com.fiuni.adoptamena.api.dao.post;

import com.fiuni.adoptamena.api.domain.post.PostReportDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostReportDao extends CrudRepository<PostReportDomain, Integer> {
    Page<PostReportDomain> findAllByIsDeletedFalse(Pageable pageable);

    @Query("SELECT p FROM PostReportDomain p WHERE " +
            "(:userId IS NULL OR p.user.id = :userId) AND " +
            "(:postId IS NULL OR p.post.id = :postId) AND " +
            "(:reportReasonsId IS NULL OR p.reportReasons.id = :reportReasonsId) AND " +
            "(:description IS NULL OR :description = '' OR p.description LIKE %:description%)"
    )
    Page<PostReportDomain> findByFiltersAndIsDeletedFalse(Pageable pageable, Integer userId, Integer postId, Integer reportReasonsId, String description);
}

