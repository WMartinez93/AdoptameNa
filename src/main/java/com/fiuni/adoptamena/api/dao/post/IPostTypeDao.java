package com.fiuni.adoptamena.api.dao.post;

import com.fiuni.adoptamena.api.domain.post.PostTypeDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPostTypeDao extends CrudRepository<PostTypeDomain, Integer> {
    Page<PostTypeDomain> findAllByIsDeletedFalse(Pageable pageable);

    Page<PostTypeDomain> findByNameContainingAndDescriptionContainingAndIsDeletedFalse(String name, String description, Pageable pageable);

    Optional<PostTypeDomain> findByIdAndIsDeletedFalse(Integer id);
}
