package com.fiuni.adoptamena.api.dao.health_state;

import com.fiuni.adoptamena.api.domain.health_state.HealthStateDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface IHealthStateDao extends CrudRepository<HealthStateDomain, Integer> {
    Page <HealthStateDomain> findAllByIsDeletedFalse(Pageable pageable);
    Optional<HealthStateDomain> findByIdAndIsDeletedFalse(Integer id);
}
