package com.fiuni.adoptamena.api.dao.pet_status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.fiuni.adoptamena.api.domain.pet_status.PetStatusDomain;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface IPetStatusDao extends CrudRepository<PetStatusDomain, Integer> {
    Page<PetStatusDomain> findAllByIsDeletedFalse(Pageable pageable);
    Optional<PetStatusDomain> findByIdAndIsDeletedFalse(Integer id);
}
