package com.fiuni.adoptamena.api.dao.race;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fiuni.adoptamena.api.domain.breed.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
@Repository
public interface IRaceDao extends CrudRepository<BreedDomain, Integer> {
    Page <BreedDomain> findAllByIsDeletedFalse(Pageable pageable);
    Optional <BreedDomain> findByIdAndIsDeletedFalse(Integer id);
}
