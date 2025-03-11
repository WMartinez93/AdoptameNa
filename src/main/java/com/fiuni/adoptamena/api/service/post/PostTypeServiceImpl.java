package com.fiuni.adoptamena.api.service.post;

import com.fiuni.adoptamena.api.dao.post.IPostTypeDao;
import com.fiuni.adoptamena.api.domain.post.PostTypeDomain;
import com.fiuni.adoptamena.api.dto.post.PostTypeDTO;
import com.fiuni.adoptamena.api.service.base.BaseServiceImpl;
import com.fiuni.adoptamena.exception_handler.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostTypeServiceImpl extends BaseServiceImpl<PostTypeDomain, PostTypeDTO> implements IPostTypeService {

    private static final Logger log = LoggerFactory.getLogger(PostTypeServiceImpl.class);
    @Autowired
    private IPostTypeDao postTypeDao;

    @Override
    public PostTypeDTO create(PostTypeDTO postTypeDto) {

        PostTypeDTO savedPostTypeDTO = null;
        try {
            PostTypeDomain postTypeDomain = convertDtoToDomain(postTypeDto);
            PostTypeDomain savedPostType = postTypeDao.save(postTypeDomain);
            log.info("PostType save successful");

            savedPostTypeDTO = convertDomainToDto(savedPostType);
        } catch (Exception e) {
            log.info("PostType save failed");
            throw new ResourceNotFoundException("Error while saving postType");
            // new ErrorResponse("Error creating a post Type", e.getMessage());
        }
        return savedPostTypeDTO;
    }

    @Override
    public PostTypeDTO update(PostTypeDTO postTypeDto) {

        PostTypeDomain savedPostTypeDomain = null;

        try {
            PostTypeDomain postTypeDomain = postTypeDao.findById(postTypeDto.getId()).orElseThrow(() -> new ResourceNotFoundException("PostType not found"));

            PostTypeDomain updatedPostTypeDomain = convertDtoToDomain(postTypeDto);

            if(!postTypeDomain.getIsDeleted()){
                updatedPostTypeDomain.setIsDeleted(false);
            }
            savedPostTypeDomain = postTypeDao.save(updatedPostTypeDomain);
            log.info("PostType update successful");
        } catch (Exception e) {
            log.info("PostType update failed");
            throw new ResourceNotFoundException("Error while updating postType");
            // new ErrorResponse("Error updating a post Type", e.getMessage());
        }
        return convertDomainToDto(savedPostTypeDomain);
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting PostType");
        try {
            PostTypeDomain postTypeDomain = postTypeDao.findById(id).orElse(null);
            if (postTypeDomain != null && !postTypeDomain.getIsDeleted()) {
                // postTypeDao.delete(postTypeDomain);// descomentar para borrar en la base de
                // datos
                postTypeDomain.setIsDeleted(true); // comentar para borrar en la base de datos
                postTypeDao.save(postTypeDomain); // comentar para borrar en la base de datos
                log.info("PostType delete successful");
            }
        } catch (Exception e) {
            log.info("PostType delete failed");
            throw new ResourceNotFoundException("Error while deleting postType");
            // new ErrorResponse("Error deleting a post Type", e.getMessage());
        }
    }

    @Override
    public PostTypeDTO getById(Integer id) {
        PostTypeDomain postType = postTypeDao.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("PostType no encontrado"));
        return convertDomainToDto(postType);
    }

    @Override
    public List<PostTypeDTO> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<PostTypeDTO> getAllPostTypes(Pageable pageable, String name, String description) {
        log.info("Get All PostType");
        if (name == null && description == null) {

            Page<PostTypeDomain> postTypesPage = postTypeDao.findAllByIsDeletedFalse(pageable);
            return convertDomainListToDtoList(postTypesPage.getContent());
        }

        Page<PostTypeDomain> postTypesPage = postTypeDao
                .findByNameContainingAndDescriptionContainingAndIsDeletedFalse(name, description, pageable);

        return convertDomainListToDtoList(postTypesPage.getContent());
    }

    @Override
    protected PostTypeDTO convertDomainToDto(PostTypeDomain postTypeDomain) {
        log.info("Converting PostTypeDomain to PostTypeDTO");

        PostTypeDTO postTypeDto = null;
        try {
            postTypeDto = new PostTypeDTO();
            postTypeDto.setId(postTypeDomain.getId());
            postTypeDto.setName(postTypeDomain.getName());
            postTypeDto.setDescription(postTypeDomain.getDescription());

        } catch (Exception e) {
            log.info("Error converting PostTypeDomain to PostTypeDTO");
            throw new ResourceNotFoundException("Error while converting PostTypeDomain to PostTypeDTO");
            // new ErrorResponse("Error converting PostTypeDomain to PostTypeDTO",
            // e.getMessage());
        }
        return postTypeDto;
    }

    @Override
    protected PostTypeDomain convertDtoToDomain(PostTypeDTO postTypeDto) {
        log.info("Converting PostTypeDTO to PostTypeDomain");

        PostTypeDomain postTypeDomain = null;
        try {
            postTypeDomain = new PostTypeDomain();
            postTypeDomain.setId(postTypeDto.getId());
            postTypeDomain.setName(postTypeDto.getName());
            postTypeDomain.setDescription(postTypeDto.getDescription());

            postTypeDomain.setIsDeleted(false);
        } catch (Exception e) {
            log.info("Error converting PostTypeDTO to PostTypeDomain");
            throw new ResourceNotFoundException("Error while converting PostTypeDTO to PostTypeDomain");
            // new ErrorResponse("Error converting PostTypeDTO to PostTypeDomain",
            // e.getMessage());
        }
        return postTypeDomain;
    }

}
