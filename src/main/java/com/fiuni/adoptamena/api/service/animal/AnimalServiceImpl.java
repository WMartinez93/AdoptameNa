package com.fiuni.adoptamena.api.service.animal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.fiuni.adoptamena.api.domain.animal.AnimalDomain;
import com.fiuni.adoptamena.api.dto.animal.AnimalDTO;
import com.fiuni.adoptamena.api.service.base.BaseServiceImpl;
import com.fiuni.adoptamena.api.dao.animal.IAnimalDao;
import com.fiuni.adoptamena.exception_handler.exceptions.*;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;



@Service
@Slf4j
public class AnimalServiceImpl extends BaseServiceImpl<AnimalDomain, AnimalDTO> implements IAnimalService {

    @Autowired
    private IAnimalDao animalDao;

    @Override
    protected AnimalDomain convertDtoToDomain(AnimalDTO dto) {
        final AnimalDomain domain = new AnimalDomain();
        domain.setId(dto.getId());
        domain.setName(dto.getName());
        return domain;
    }

    @Override
    protected AnimalDTO convertDomainToDto(AnimalDomain domain) {
        final AnimalDTO dto = new AnimalDTO();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        return dto;
    }

    @Override
    @Transactional
    public AnimalDTO create(AnimalDTO dto) {
        AnimalDomain domain = new AnimalDomain();
        domain.setName(dto.getName());
        domain.setIsDeleted(false);
        domain = animalDao.save(domain);
        return convertDomainToDto(domain);
    }

    @Override
    @Transactional
    public AnimalDTO update(AnimalDTO dto) {
        AnimalDomain domain = animalDao.findByIdAndIsDeletedFalse(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el aniaml"));
        domain.setName(dto.getName());
        domain = animalDao.save(domain);
        return convertDomainToDto(domain);
    }

    @Override
    public AnimalDTO getById(Integer id) {
        AnimalDomain domain = animalDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el animal"));
        return convertDomainToDto(domain);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        AnimalDomain domain = animalDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el aniaml"));
        domain.setIsDeleted(true);
        animalDao.save(domain);
    }

    @Override
    public List<AnimalDTO> getAll(Pageable pageable) {
        Page<AnimalDomain> domain = animalDao.findAllByIsDeletedFalse(pageable);
        return convertDomainListToDtoList(domain.getContent());
    }
}
