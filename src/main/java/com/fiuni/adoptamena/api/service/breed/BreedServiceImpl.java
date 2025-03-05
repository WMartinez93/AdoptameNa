package com.fiuni.adoptamena.api.service.breed;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fiuni.adoptamena.api.service.base.BaseServiceImpl;
import com.fiuni.adoptamena.api.dao.animal.IAnimalDao;
import com.fiuni.adoptamena.api.dao.race.*;
import com.fiuni.adoptamena.api.domain.breed.*;
import com.fiuni.adoptamena.api.dto.breed.*;
import com.fiuni.adoptamena.exception_handler.exceptions.*;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BreedServiceImpl extends BaseServiceImpl<BreedDomain, BreedDTO> implements IBreedService {

    @Autowired
    private IRaceDao raceDao;

    @Autowired
    private IAnimalDao animalDao;

    @Override
    protected BreedDomain convertDtoToDomain(BreedDTO dto) {
        BreedDomain domain = new BreedDomain();
        domain.setId(dto.getId());
        domain.setName(dto.getName());
        return domain;
    }

    @Override
    protected BreedDTO convertDomainToDto(BreedDomain domain) {
        BreedDTO dto = new BreedDTO();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        dto.setAnimalId(domain.getAnimal().getId());
        return dto;
    }

    @Override
    @Transactional
    public BreedDTO create(BreedDTO dto) {
        log.info("llega al service");
        BreedDomain domain = new BreedDomain();
        domain.setAnimal(animalDao.findByIdAndIsDeletedFalse(dto.getAnimalId())
                .orElseThrow(() -> new ResourceNotFoundException("Animal no encontrado")));
        log.info("pasa verificacion de animal");
        domain.setName(dto.getName());
        domain.setIsDeleted(false);
        domain = raceDao.save(domain);
        return convertDomainToDto(domain);
    }

    @Override
    @Transactional
    public BreedDTO update(BreedDTO dto) {
        BreedDomain domain = raceDao.findByIdAndIsDeletedFalse(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Raza no encontrada"));
        domain.setAnimal(animalDao.findByIdAndIsDeletedFalse(dto.getAnimalId())
                .orElseThrow(() -> new ResourceNotFoundException("Animal no encontrado")));
        
                domain.setName(dto.getName());
        domain = raceDao.save(domain);
        return convertDomainToDto(domain);
    }

    @Override
    public BreedDTO getById(Integer id) {
        BreedDomain domain = raceDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raza no encontrada"));
        return convertDomainToDto(domain);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        BreedDomain domain = raceDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raza no encontrada"));
        domain.setIsDeleted(true);
        raceDao.save(domain);
        return;
    }

    @Override
    public List<BreedDTO> getAll(Pageable pageable) {
        Page<BreedDomain> domain = raceDao.findAllByIsDeletedFalse(pageable);
        return convertDomainListToDtoList(domain.getContent());
    }

}
