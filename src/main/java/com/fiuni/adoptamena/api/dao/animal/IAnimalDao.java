package com.fiuni.adoptamena.api.dao.animal;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fiuni.adoptamena.api.domain.animal.AnimalDomain;

@Repository
public interface IAnimalDao extends CrudRepository<AnimalDomain, Integer> {
    Page<AnimalDomain> findAllByIsDeletedFalse(Pageable pageable);
    Optional<AnimalDomain> findByIdAndIsDeletedFalse(Integer id);
}
