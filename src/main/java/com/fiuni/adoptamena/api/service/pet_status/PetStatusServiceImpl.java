package com.fiuni.adoptamena.api.service.pet_status;

import com.fiuni.adoptamena.api.domain.pet_status.PetStatusDomain;
import com.fiuni.adoptamena.api.service.base.BaseServiceImpl;

import jakarta.transaction.Transactional;

import com.fiuni.adoptamena.api.dto.pet_status.PetStatusDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fiuni.adoptamena.api.dao.pet_status.IPetStatusDao;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;
import com.fiuni.adoptamena.exception_handler.exceptions.*;

@Service
public class PetStatusServiceImpl extends BaseServiceImpl<PetStatusDomain, PetStatusDTO> implements IPetStatusService {
    @Autowired
    private IPetStatusDao petStatusDao;
    
    @Override
    protected PetStatusDomain convertDtoToDomain(PetStatusDTO dto) {
        final PetStatusDomain domain = new PetStatusDomain();
        domain.setId(dto.getId());
        domain.setName(dto.getName());
        domain.setDescription(dto.getDescription());
        return domain;
    }
    @Override
    protected PetStatusDTO convertDomainToDto(PetStatusDomain domain) {
        final PetStatusDTO dto = new PetStatusDTO();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        dto.setDescription(domain.getDescription());
        return dto;
    }
    @Override
    @Transactional
    public PetStatusDTO create(PetStatusDTO dto) {
        PetStatusDomain domain = new PetStatusDomain();
        domain.setName(dto.getName());
        domain.setDescription(dto.getDescription());
        domain.setIsDeleted(false);
        domain = petStatusDao.save(domain);
        return convertDomainToDto(domain);
    }
    
    @Override
    @Transactional
    public PetStatusDTO update(PetStatusDTO dto) {
        PetStatusDomain domain = petStatusDao.findByIdAndIsDeletedFalse(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el estado de mascota"));
        domain.setName(dto.getName());
        domain.setDescription(dto.getDescription());
        domain = petStatusDao.save(domain);
        return convertDomainToDto(domain);
    }

    @Override
    public PetStatusDTO getById(Integer id) {
        PetStatusDomain domain = petStatusDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el estado de mascota"));
        return convertDomainToDto(domain);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        PetStatusDomain domain = petStatusDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el estado de mascota"));
        domain.setIsDeleted(true);
        petStatusDao.save(domain);
        return;
    }
    @Override
    public List<PetStatusDTO> getAll(Pageable pageable) {
        Page<PetStatusDomain> domain = petStatusDao.findAllByIsDeletedFalse(pageable);
        return convertDomainListToDtoList(domain.getContent());
    }
    
}
