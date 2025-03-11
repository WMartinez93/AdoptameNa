package com.fiuni.adoptamena.api.dao.post;

import com.fiuni.adoptamena.api.domain.post.PostDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostDao extends CrudRepository<PostDomain, Integer> {
    @Query("SELECT p FROM PostDomain p WHERE " +
            "(COALESCE(:title, '') = '' OR p.title LIKE %:title%) AND " +
            "(COALESCE(:content, '') = '' OR p.content LIKE %:content%) AND " +
            "(COALESCE(:userId, NULL) IS NULL OR p.user.id = :userId) AND " +
            "(COALESCE(:postTypeName, '') = '' OR p.postType.name LIKE %:postTypeName%)")
    Page<PostDomain> findByFiltersAAndIsDeletedFalse(Pageable pageable, String title, String content, Integer userId, String postTypeName);

    Page<PostDomain> findAllByIsDeletedFalse(Pageable pageable);

    Page<PostDomain> findByTitleContainingOrContentContainingAndIsDeletedFalse(String keyword, String keyword1, Pageable pageable);
}
