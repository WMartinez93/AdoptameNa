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
/*
    @Query("SELECT p FROM PostReportDomain p WHERE " +
            "(COALESCE(:userId, NULL) = IS NULL OR p.user.id = :userId) AND " +
            "(COALESCE(:postId, NULL) = IS NULL OR p.post.id = :postId) AND " +
            "(COALESCE(:reportReasonsId, NULL) IS NULL OR p.reportReason.id = :reportReasonsId) AND " +
            "(COALESCE(:description, '') = '' OR p.descripcion LIKE %:description%)")
    Page<PostReportDomain> findByFiltersAndIsDeletedFalse(Pageable pageable, Integer userId, Integer postId, Integer reportReasonsId, String description);*/
}

