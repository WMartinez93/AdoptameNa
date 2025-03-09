package com.fiuni.adoptamena.api.service.profile;

import com.fiuni.adoptamena.api.dao.profile.IProfileDao;
import com.fiuni.adoptamena.api.dao.user.IUserDao;
import com.fiuni.adoptamena.api.domain.profile.*;
import com.fiuni.adoptamena.api.domain.user.UserDomain;
import com.fiuni.adoptamena.api.dto.profile.ProfileDTO;
import com.fiuni.adoptamena.api.dto.user.UserProfileDTO;
import com.fiuni.adoptamena.api.service.base.BaseServiceImpl;
import com.fiuni.adoptamena.exception_handler.exceptions.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Service
@Slf4j
public class ProfileServiceImpl extends BaseServiceImpl<ProfileDomain, ProfileDTO> implements IProfileService {

    @Autowired
    private IProfileDao profileDao;
    @Autowired
    private IUserDao userDao;

    @Override
    protected ProfileDomain convertDtoToDomain(ProfileDTO dto) {
        ProfileDomain domain = new ProfileDomain();
        domain.setId(dto.getId());
        domain.setOrganizationName(dto.getOrganizationName());
        domain.setFullName(dto.getFullName());
        domain.setAddress(dto.getAddress());
        domain.setDescription(dto.getDescription());
        domain.setGender(dto.getGender());
        domain.setBirthdate(dto.getBirthdate());
        domain.setDocument(dto.getDocument());
        domain.setPhoneNumber(dto.getPhoneNumber());
        domain.setEarnedPoints(dto.getEarnedPoints());
        return domain;
    }

    @Override
    protected ProfileDTO convertDomainToDto(ProfileDomain domain) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(domain.getId());
        dto.setOrganizationName(domain.getOrganizationName());
        dto.setFullName(domain.getFullName());
        dto.setAddress(domain.getAddress());
        dto.setDescription(domain.getDescription());
        dto.setGender(domain.getGender());
        dto.setBirthdate(domain.getBirthdate());
        dto.setDocument(domain.getDocument());
        dto.setPhoneNumber(domain.getPhoneNumber());
        dto.setEarnedPoints(domain.getEarnedPoints());
        return dto;
    }

    @Override
    public List<ProfileDTO> getAll(Pageable pageable) {
        return convertDomainListToDtoList(profileDao.findAllByIsDeletedFalse(pageable).getContent());
    }

    @Override
    public ProfileDTO getById(Integer userId) {

        UserDomain user = userDao.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return convertDomainToDto(user.getProfile());
    }

    @Override
    @Transactional
    public ProfileDTO create(ProfileDTO profile) {
        log.info("Creando perfil para el usuario con ID {}", profile.getId());
        UserDomain user = userDao.findByIdAndIsDeletedFalse(profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (user.getProfile() != null) {
            throw new BadRequestException("El usuario ya tiene un perfil");
        }

        ProfileDomain domain = convertDtoToDomain(profile);
        domain.setUser(user);
        domain.setId(null);
        setDefaultAttributes(domain);
        // TEST
        log.info("ProfileDomain a ser guardado: {}", domain);
        ProfileDomain saved = profileDao.save(domain);
        log.info("Perfil creado para el usuario con ID {}", profile.getId());
        return convertDomainToDto(saved);
        // return convertDomainToDto(profileDao.save(domain));
    }

    @Override
    @Transactional
    public ProfileDTO update(ProfileDTO profile) {
        log.info("Actualizando perfil del usuario con ID {}", profile.getId());
        UserDomain user = userDao.findByIdAndIsDeletedFalse(profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        ProfileDomain profileDomain = user.getProfile();
        if (profileDomain == null) {
            throw new ResourceNotFoundException("Perfil no encontrado");
        }

        if (profile.getGender() != null) {
            validateGender(profile.getGender().name());
        }

        profileDomain.setFullName(profile.getFullName());
        profileDomain.setOrganizationName(profile.getOrganizationName());
        profileDomain.setAddress(profile.getAddress());
        profileDomain.setDescription(profile.getDescription());
        profileDomain.setGender(profile.getGender());
        profileDomain.setBirthdate(profile.getBirthdate());
        profileDomain.setDocument(profile.getDocument());
        profileDomain.setPhoneNumber(profile.getPhoneNumber());

        return convertDomainToDto(profileDao.save(profileDomain));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        UserDomain user = userDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        ProfileDomain domain = user.getProfile();
        if (domain == null) {
            throw new ResourceNotFoundException("Perfil no encontrado");
        }
        domain.setIsDeleted(true);
        profileDao.save(domain);
        log.info("Perfil eliminado para el usuario con ID {}", id);
    }

    private ProfileDomain setDefaultAttributes(ProfileDomain domain) {
        domain.setAddress(null);
        domain.setDescription(null);
        domain.setBirthdate(null);
        domain.setGender(null);
        domain.setDocument(null);
        domain.setPhoneNumber(null);
        domain.setEarnedPoints(0);
        domain.setIsDeleted(false);
        domain.setAddressCoordinates(null);
        return domain;
    }

    private void validateGender(String gender) {
        try {
            EnumGender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(gender + " no es un género válido");
        }
    }

    @Override
    public UserProfileDTO getUserProfileDTO(Integer id) {
        UserProfileDTO dto = new UserProfileDTO();
        ProfileDomain profile = userDao.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado")).getProfile();
        UserDomain user = userDao.findByIdAndIsDeletedFalse(id).orElseThrow(
                () -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));

        dto.setId(profile.getId());
        dto.setFullName(profile.getFullName());
        dto.setEmail(user.getEmail());
        dto.setCreationDate(user.getCreationDate());
        dto.setRole(user.getRole().getName());
        dto.setIsVerified(user.getIsVerified());
        return dto;
    }

}
