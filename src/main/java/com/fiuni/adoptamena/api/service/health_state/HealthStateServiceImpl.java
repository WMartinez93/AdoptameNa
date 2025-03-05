package com.fiuni.adoptamena.api.service.health_state;

import com.fiuni.adoptamena.api.domain.health_state.HealthStateDomain;
import com.fiuni.adoptamena.api.dto.health_state.HealthStateDTO;
import com.fiuni.adoptamena.api.service.base.BaseServiceImpl;

import jakarta.transaction.Transactional;

import com.fiuni.adoptamena.api.dao.health_state.IHealthStateDao;
import com.fiuni.adoptamena.exception_handler.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
public class HealthStateServiceImpl extends BaseServiceImpl<HealthStateDomain, HealthStateDTO> implements IHealthStateService {
    @Autowired
    private IHealthStateDao healthStateDao;

    @Override
    protected HealthStateDomain convertDtoToDomain(HealthStateDTO dto) {
        HealthStateDomain domain = new HealthStateDomain();
        domain.setId(dto.getId());
        domain.setName(dto.getName());
        return domain;
    }

    @Override
    protected HealthStateDTO convertDomainToDto(HealthStateDomain domain) {
        HealthStateDTO dto = new HealthStateDTO();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        return dto;
    }

    @Override
    @Transactional
    public HealthStateDTO create(HealthStateDTO dto) {
        HealthStateDomain domain = new HealthStateDomain();
        domain.setName(dto.getName());
        domain.setIsDeleted(false);
        domain = healthStateDao.save(domain);
        return convertDomainToDto(domain);
    }

    @Override
    public HealthStateDTO getById(Integer id) {
        HealthStateDomain healthState = healthStateDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro health state"));
        return convertDomainToDto(healthState);
    }

    @Override
    @Transactional
    public HealthStateDTO update(HealthStateDTO dto) {
        HealthStateDomain domain = healthStateDao.findByIdAndIsDeletedFalse(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro health state"));
        domain.setName(dto.getName());
        domain = healthStateDao.save(domain);
        return convertDomainToDto(domain);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        HealthStateDomain healthState = healthStateDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro health state"));
        healthState.setIsDeleted(true);
        healthStateDao.save(healthState);
        return;
    }

    @Override
    public List<HealthStateDTO> getAll(Pageable pageable) {
        Page<HealthStateDomain> domain = healthStateDao.findAllByIsDeletedFalse(pageable);
        return convertDomainListToDtoList(domain.getContent());
    }

}
