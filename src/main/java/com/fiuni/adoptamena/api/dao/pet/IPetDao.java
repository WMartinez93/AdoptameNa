package com.fiuni.adoptamena.api.dao.pet;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fiuni.adoptamena.api.domain.pet.PetDomain;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Repository
public interface IPetDao extends CrudRepository<PetDomain, Integer> {
    Optional<PetDomain> findByIdAndIsDeletedFalse(Integer id);
    Page<PetDomain> findAllByIsDeletedFalse(Pageable pageable);
}
