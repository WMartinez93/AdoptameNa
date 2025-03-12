package com.fiuni.adoptamena.api.dao.favorite;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.fiuni.adoptamena.api.domain.favorite.FavoriteDomain;

public interface IFavoriteDao extends CrudRepository<FavoriteDomain, Integer> {

    Page<FavoriteDomain> findAll(Pageable pageable);

    Optional<FavoriteDomain> findByPostIdAndUserId(Integer postId, Integer userId);

    Page<FavoriteDomain> findAllByUserId(Integer userId, Pageable pageable);

}