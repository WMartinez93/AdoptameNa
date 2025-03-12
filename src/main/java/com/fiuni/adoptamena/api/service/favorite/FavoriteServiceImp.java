package com.fiuni.adoptamena.api.service.favorite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fiuni.adoptamena.api.dao.favorite.IFavoriteDao;
import com.fiuni.adoptamena.api.dao.post.IPostDao;
import com.fiuni.adoptamena.api.dao.user.IUserDao;
import com.fiuni.adoptamena.api.domain.favorite.FavoriteDomain;
import com.fiuni.adoptamena.api.domain.user.UserDomain;
import com.fiuni.adoptamena.api.domain.post.PostDomain;
import com.fiuni.adoptamena.api.dto.favorite.FavoriteDTO;
import com.fiuni.adoptamena.api.dto.post.PostDTO;
import com.fiuni.adoptamena.api.dto.post.ResponsePostDTO;
import com.fiuni.adoptamena.api.service.base.BaseServiceImpl;
import com.fiuni.adoptamena.auth.CustomUserDetails;
import com.fiuni.adoptamena.exception_handler.exceptions.BadRequestException;
import com.fiuni.adoptamena.exception_handler.exceptions.ForbiddenException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FavoriteServiceImp extends BaseServiceImpl<FavoriteDomain, FavoriteDTO> implements IFavoriteService {

    @Autowired
    private IFavoriteDao favoriteDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IPostDao postDao;

    @Override
    public FavoriteDTO getById(Integer id) {
        FavoriteDomain domain = favoriteDao.findById(id).orElse(null);
        return domain != null ? convertDomainToDto(domain) : null;
    }

    @Override
    public List<FavoriteDTO> getAll(Pageable pageable) {
        return favoriteDao.findAll(pageable)
                .stream()
                .map(this::convertDomainToDto)
                .toList();
    }

    /*
     * En realidad para los favoritos se deberia eliminar el registro de la tabla ya
     * que no tiene sentido tener un registro de un favorito eliminado
     * pero se opto por esta solucion para mantener la integridad de la base de
     * datos y la consistencia de los servicios
     */
    @Override
    public void delete(Integer id) {
        FavoriteDomain domain = favoriteDao.findById(id)
                .orElseThrow(() -> new BadRequestException("No se encontró el favorito"));

        if (!isCurrentUserOwner(domain)) {
            throw new BadRequestException("No puedes eliminar un favorito que no te pertenece");
        }

        favoriteDao.delete(domain);

    }

    @Override
    public FavoriteDTO create(FavoriteDTO dto) {
        UserDomain currentUser = getCurrentUser();

        FavoriteDomain existingDomain = favoriteDao
                .findByPostIdAndUserId(dto.getPostId(), currentUser.getId()).orElse(null);
        if (existingDomain != null) {
            throw new BadRequestException("El favorito ya existe");
        }

        FavoriteDomain domain = convertDtoToDomain(dto);
        domain.setUser(currentUser);

        FavoriteDomain savedDomain = favoriteDao.save(domain);
        return convertDomainToDto(savedDomain);
    }

    @Override
    public FavoriteDTO update(FavoriteDTO dto) {
        FavoriteDomain existingDomain = favoriteDao.findById(dto.getId()).orElse(null);
        if (existingDomain == null) {
            return null;
        }

        if (!isCurrentUserOwner(existingDomain)) {
            throw new BadRequestException("No puedes modificar un favorito que no te pertenece");
        }

        FavoriteDomain updatedDomain = convertDtoToDomain(dto);
        updatedDomain.setUser(existingDomain.getUser());

        return convertDomainToDto(favoriteDao.save(updatedDomain));
    }

    @Override
    protected FavoriteDTO convertDomainToDto(FavoriteDomain domain) {
        FavoriteDTO dto = new FavoriteDTO();

        dto.setId(domain.getId());
        dto.setPostId(domain.getPost().getId());
        ResponsePostDTO post = new ResponsePostDTO();
        post.setId(domain.getPost().getId());
        post.setTitle(domain.getPost().getTitle());
        post.setContent(domain.getPost().getContent());
        post.setPostTypeName(domain.getPost().getPostType().getName());
        post.setContactNumber(domain.getPost().getContactNumber());
        post.setLocationCoordinates(domain.getPost().getLocationCoordinates());
        post.setPublicationDate(domain.getPost().getPublicationDate());
        post.setSharedCounter(domain.getPost().getSharedCounter());
        post.setStatus(domain.getPost().getStatus());
        post.setIdUser(domain.getPost().getUser().getId());

        dto.setPost(post);
        return dto;
    }

    @Override
    protected FavoriteDomain convertDtoToDomain(FavoriteDTO dto) {
        FavoriteDomain domain = new FavoriteDomain();
        if (dto.getId() != null) {
            domain.setId(dto.getId());
        }

        PostDomain post = postDao.findById(dto.getPostId()).orElse(null);
        domain.setPost(post);

        return domain;
    }

    public List<FavoriteDTO> getAllByUserId(Pageable pageable) {
        UserDomain currentUser = getCurrentUser();

        if (currentUser == null) {
            return List.of();
        }

        return favoriteDao.findAllByUserId(currentUser.getId(), pageable)
                .stream()
                .map(this::convertDomainToDto)
                .toList();
    }

    private boolean isCurrentUserOwner(FavoriteDomain favorite) {
        UserDomain currentUser = getCurrentUser();
        return currentUser != null && favorite.getUser().getId().equals(currentUser.getId());
    }

    /*
     * Obtiene el usuario actualmente logueado, se implementa aqui, ya que en la
     * Interfaz base no se pasa el id del usuario como parametro, entonces no es
     * posible pasarlo por controlador se podria pasar desde el controlador
     * modificando el dto que se recibe, pero se opto por esta solucion
     */
    private UserDomain getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Integer userId = userDetails.getId();
            return userDao.findByIdAndIsDeletedFalse(userId)
                    .orElseThrow(() -> new BadRequestException("No se encontró el usuario logueado"));
        }
        throw new ForbiddenException("Usuario no autenticado");
    }
}