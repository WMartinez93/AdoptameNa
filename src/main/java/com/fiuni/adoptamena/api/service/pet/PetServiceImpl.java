package com.fiuni.adoptamena.api.service.pet;

import com.fiuni.adoptamena.api.dao.pet.IPetDao;
import com.fiuni.adoptamena.api.domain.pet.PetDomain;
import com.fiuni.adoptamena.api.dto.pet.PetDTO;
import com.fiuni.adoptamena.api.service.base.BaseServiceImpl;
import com.fiuni.adoptamena.exception_handler.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fiuni.adoptamena.api.dao.animal.IAnimalDao;
import com.fiuni.adoptamena.api.dao.user.*;
import com.fiuni.adoptamena.api.dao.race.*;
import com.fiuni.adoptamena.api.dao.health_state.*;
import com.fiuni.adoptamena.api.dao.pet_status.*;
import java.util.List;

@Service
public class PetServiceImpl extends BaseServiceImpl<PetDomain, PetDTO> implements IPetService {

    @Autowired
    private IPetDao petDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IAnimalDao animalDao;

    @Autowired
    private IRaceDao raceDao;

    @Autowired
    private IHealthStateDao healthStateDao;

    @Autowired
    private IPetStatusDao petStatusDao;

    @Override
    protected PetDomain convertDtoToDomain(PetDTO dto) {
        PetDomain domain = new PetDomain();
        domain.setId(dto.getId());
        domain.setName(dto.getName());
        domain.setIsVaccinated(dto.getIsVaccinated());
        domain.setDescription(dto.getDescription());
        domain.setBirthdate(dto.getBirthdate());
        domain.setGender(dto.getGender());
        domain.setUrlPhoto(dto.getUrlPhoto());
        domain.setIsSterilized(dto.getIsSterilized());
        return domain;
    }

    @Override
    protected PetDTO convertDomainToDto(PetDomain domain) {
        PetDTO dto = new PetDTO();
        dto.setId(domain.getId());
        dto.setName(domain.getName());
        dto.setIsVaccinated(domain.getIsVaccinated());
        dto.setDescription(domain.getDescription());
        dto.setBirthdate(domain.getBirthdate());
        dto.setGender(domain.getGender());
        dto.setUrlPhoto(domain.getUrlPhoto());
        dto.setIsSterilized(domain.getIsSterilized());

        dto.setUserId(domain.getUser().getId());
        dto.setAnimalId(domain.getAnimal().getId());
        dto.setBreedId(domain.getRace().getId());
        dto.setHealthStateId(domain.getHealthState().getId());
        dto.setPetStatusId(domain.getPetStatus().getId());

        return dto;
    }

    @Override
    @Transactional
    public PetDTO create(PetDTO dto) {
        PetDomain petDomain = convertDtoToDomain(dto);
        petDomain.setIsDeleted(false);

        petDomain.setUser(userDao.findByIdAndIsDeletedFalse(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado")));

        petDomain.setAnimal(animalDao.findByIdAndIsDeletedFalse(dto.getAnimalId())
                .orElseThrow(() -> new ResourceNotFoundException("Animal no encontrado")));

        petDomain.setRace(raceDao.findByIdAndIsDeletedFalse(dto.getBreedId())
                .orElseThrow(() -> new ResourceNotFoundException("Raza no encontrada")));

        petDomain.setHealthState(healthStateDao.findByIdAndIsDeletedFalse(dto.getHealthStateId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado de salud no encontrado")));
                
        petDomain.setPetStatus(petStatusDao.findByIdAndIsDeletedFalse(dto.getPetStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado de mascota no encontrado")));

        petDomain = petDao.save(petDomain);
        return convertDomainToDto(petDomain);
    }

    @Override
    public PetDTO getById(Integer id) {
        PetDomain pet = petDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la mascota"));
        return convertDomainToDto(pet);
    }

    @Override
    @Transactional
    public PetDTO update(PetDTO dto) {
        PetDomain pet = petDao.findByIdAndIsDeletedFalse(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la mascota"));
        pet.setName(dto.getName());
        pet.setIsVaccinated(dto.getIsVaccinated());
        pet.setDescription(dto.getDescription());
        pet.setBirthdate(dto.getBirthdate());
        pet.setGender(dto.getGender());
        pet.setUrlPhoto(dto.getUrlPhoto());
        pet.setIsSterilized(dto.getIsSterilized());
        pet = petDao.save(pet);
        return convertDomainToDto(pet);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        PetDomain pet = petDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la mascota"));
        pet.setIsDeleted(true);
        petDao.save(pet);
    }

    @Override
    public List<PetDTO> getAll(Pageable pageable) {
        Page<PetDomain> domain = petDao.findAllByIsDeletedFalse(pageable);
        return convertDomainListToDtoList(domain.getContent());
    }
}
