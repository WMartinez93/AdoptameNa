package com.fiuni.adoptamena.api.dao.favorite;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.fiuni.adoptamena.api.domain.favorite.FavoriteDomain;

public interface IFavoriteDao extends CrudRepository<FavoriteDomain, Integer> {
    Optional<FavoriteDomain> findByIdAndIsDeletedFalse(Integer id);

    Page<FavoriteDomain> findAllByIsDeletedFalse(Pageable pageable);

    Optional<FavoriteDomain> findByPostIdAndUserIdAndIsDeletedFalse(Integer postId, Integer userId);

    Page<FavoriteDomain> findAllByUserIdAndIsDeletedFalse(Integer userId, Pageable pageable);

}