package com.fiuni.adoptamena.api.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.fiuni.adoptamena.api.dao.profile.IProfileDao;
import com.fiuni.adoptamena.api.dao.user.IRoleDao;
import com.fiuni.adoptamena.api.dao.user.IUserDao;
import com.fiuni.adoptamena.api.domain.user.UserDomain;
import com.fiuni.adoptamena.api.domain.profile.ProfileDomain;
import com.fiuni.adoptamena.api.domain.user.RoleDomain;
import com.fiuni.adoptamena.api.dto.user.UserDTO;
import com.fiuni.adoptamena.api.dto.user.UserProfileDTO;
import com.fiuni.adoptamena.api.service.base.BaseServiceImpl;
import com.fiuni.adoptamena.api.service.profile.IProfileService;
import com.fiuni.adoptamena.exception_handler.exceptions.BadRequestException;
import com.fiuni.adoptamena.exception_handler.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<UserDomain, UserDTO> implements IUserService {

    @Autowired
    private IUserDao userDao;
    @Autowired
    private IProfileDao profileDao;
    @Autowired
    private IRoleDao roleDao;
    @Autowired
    private IProfileService profileService;

    @Override
    protected UserDTO convertDomainToDto(UserDomain domain) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(domain.getId());
        userDTO.setEmail(domain.getEmail());
        userDTO.setRole(domain.getRole().getName());
        userDTO.setCreationDate(domain.getCreationDate());
        userDTO.setIsVerified(domain.getIsVerified());
        return userDTO;
    }

    @Override
    protected UserDomain convertDtoToDomain(UserDTO dto) {
        UserDomain userDomain = new UserDomain();
        userDomain.setId(dto.getId());
        userDomain.setEmail(dto.getEmail());

        RoleDomain role = roleDao.findByName(dto.getRole())
                .orElseThrow(() -> new BadRequestException("Rol no encontrado " + dto.getRole()));

        userDomain.setRole(role);
        return userDomain;
    }

    @Override
    public UserDTO getById(Integer id) {
        UserDomain user = userDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return convertDomainToDto(user);
    }

    @Override
    public List<UserDTO> getAll(Pageable pageable) {
        return convertDomainListToDtoList(userDao.findAllByIsDeletedFalse(pageable).getContent());
    }

    @Override
    public void delete(Integer id) {
        UserDomain user = userDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        user.setIsDeleted(true);

        // Borrar perfil tambien
        profileService.delete(user.getProfile().getId());

        // Guardar cambios
        userDao.save(user);
    }

    @Override
    public UserDTO create(UserDTO dto) {
        UserDomain user = convertDtoToDomain(dto);
        return convertDomainToDto(userDao.save(user));
    }

    @Override
    public UserDTO update(UserDTO dto) {
        UserDomain user = userDao.findByIdAndIsDeletedFalse(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        user.setUsername(dto.getEmail());
        user.setEmail(dto.getEmail());

        RoleDomain role = roleDao.findByName(dto.getRole())
                .orElseThrow(() -> new BadRequestException("Rol no encontrado " + dto.getRole()));
        user.setRole(role);
        return convertDomainToDto(userDao.save(user));
    }

    @Override
    public UserProfileDTO getUserProfileDTO(Integer id) {
        UserDomain user = userDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        ProfileDomain profile = profileDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado"));

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(user.getId());
        userProfileDTO.setEmail(user.getEmail());
        userProfileDTO.setRole(user.getRole().getName());
        userProfileDTO.setCreationDate(user.getCreationDate());
        userProfileDTO.setIsVerified(user.getIsVerified());
        userProfileDTO.setFullName(profile.getFullName());

        return userProfileDTO;
    }

}
