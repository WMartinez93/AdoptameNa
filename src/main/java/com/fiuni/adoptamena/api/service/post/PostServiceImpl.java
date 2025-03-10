package com.fiuni.adoptamena.api.service.post;

import com.fiuni.adoptamena.api.dao.post.IPostDao;
import com.fiuni.adoptamena.api.dao.post.IPostTypeDao;
import com.fiuni.adoptamena.api.dao.user.IUserDao;
import com.fiuni.adoptamena.api.domain.post.PostDomain;
import com.fiuni.adoptamena.api.domain.post.PostTypeDomain;
import com.fiuni.adoptamena.api.domain.user.UserDomain;
import com.fiuni.adoptamena.api.dto.post.PostDTO;
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
import java.util.Optional;

@Service
@Transactional
public class PostServiceImpl extends BaseServiceImpl<PostDomain, PostDTO> implements IPostService {
    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private IPostDao postDao;

    @Autowired
    private IPostTypeDao postTypeDao;

    @Autowired
    private IUserDao userDao;

    @Override
    public PostDTO create(PostDTO postDto) {
        PostDTO savedPostDTO = null;
        try {
            PostDomain postDomain = convertDtoToDomain(postDto);
            PostDomain savedPostDomain = postDao.save(postDomain);
            log.info("post save successful");

            savedPostDTO = convertDomainToDto(savedPostDomain);
        } catch (Exception e) {
            log.info("post save failed");
            throw new ResourceNotFoundException("Post could not be saved");
            // new ErrorResponse("Error creating post", e.getMessage());
        }
        return savedPostDTO;
    }

    @Override
    public PostDTO update(PostDTO postDto) {
        PostDomain savedPostDomain = null;
        try {
            PostDomain postDomain = postDao.findById(postDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Post could not be found"));

            PostDomain updatePostDomain = convertDtoToDomain(postDto);

            if (!postDomain.getIsDeleted()) {
                updatePostDomain.setPublicationDate(postDomain.getPublicationDate());
                updatePostDomain.setIsDeleted(false);
                updatePostDomain.setUser(postDomain.getUser());
                updatePostDomain.setStatus(postDomain.getStatus());
            }

            savedPostDomain = postDao.save(updatePostDomain);
            log.info("post update successful");
        } catch (Exception e) {
            log.info("post update failed");
            throw new ResourceNotFoundException("Post could not be updated");
            // new ErrorResponse("Error updating post", e.getMessage());
        }
        return convertDomainToDto(savedPostDomain);
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting post");
        try {
            PostDomain postDomain = postDao.findById(id).orElse(null);
            if (postDomain != null && !postDomain.getIsDeleted()) {
                // postDao.delete(postDomain); // descomentar para borrar en la base de datos
                postDomain.setIsDeleted(true); // comentar para borrar en la base de datos
                postDao.save(postDomain); // comentar para borrar en la base de datos
                log.info("post delete successful");
            }
        } catch (Exception e) {
            log.info("post delete failed");
            throw new ResourceNotFoundException("Post could not be deleted");
            // new ErrorResponse("Error deleting post", e.getMessage());
        }
    }

    @Override
    public PostDTO getById(Integer id) {
        log.info("Getting post by id");
        PostDTO postDto = null;
        try {
            Optional<PostDomain> postDomainOptional = postDao.findById(id);
            if (postDomainOptional.isPresent() && !postDomainOptional.get().getIsDeleted()) {
                PostDomain postDomain = postDomainOptional.get();
                postDto = convertDomainToDto(postDomain);
                log.info("post get successful");
            }
        } catch (Exception e) {
            log.info("post get failed");
            throw new ResourceNotFoundException("Post could not be found");
            // new ErrorResponse("Error getting post", e.getMessage());
        }
        return postDto;
    }

    @Override
    public List<PostDTO> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<PostDTO> getAllPosts(Pageable pageable, String title, String content, Integer userId,
            String postTypeName) {
        log.info("Getting all posts");

        if (title != null || content != null || userId != null || postTypeName != null) {
            Page<PostDomain> postPage = postDao.findByFiltersAAndIsDeletedFalse(pageable, title, content, userId,
                    postTypeName);
            return convertDomainListToDtoList(postPage.getContent());
        }

        Page<PostDomain> postPage = postDao.findAllByIsDeletedFalse(pageable);
        return convertDomainListToDtoList(postPage.getContent());
    }

    @Override
    public List<PostDTO> searchPostByKeyword(Pageable pageable, String keyword) {
        log.info("Searching posts by keyword: {}", keyword);

        Page<PostDomain> postPage = postDao.findByTitleContainingOrContentContainingAndIsDeletedFalse(keyword, keyword,
                pageable);

        return convertDomainListToDtoList(postPage.getContent());
    }

    @Override
    public void increaseSharedCounter(Integer postId) {

        PostDomain postDomain = postDao.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        postDomain.setSharedCounter(postDomain.getSharedCounter() + 1);

        postDao.save(postDomain);
    }

    @Override
    protected PostDTO convertDomainToDto(PostDomain postDomain) {
        log.info("Converting PostDomain to PostDto");

        PostDTO postDto = null;
        try {
            postDto = new PostDTO();
            postDto.setId(postDomain.getId());
            postDto.setTitle(postDomain.getTitle());
            postDto.setContent(postDomain.getContent());
            postDto.setPublicationDate(postDomain.getPublicationDate());
            postDto.setContactNumber(postDomain.getContactNumber());
            postDto.setLocationCoordinates(postDomain.getLocationCoordinates());
            postDto.setSharedCounter(postDomain.getSharedCounter());
            postDto.setStatus(postDomain.getStatus());

            if (postDomain.getUser() != null && postDomain.getUser().getId() != null) {
                postDto.setIdUser(postDomain.getUser().getId());
            } else {
                log.info("User not found");
                throw new ResourceNotFoundException("User not found");
            }

            if (postDomain.getPostType() != null && postDomain.getPostType().getId() != null) {
                postDto.setIdPostType(postDomain.getPostType().getId());
            } else {
                log.info("PostType not found");
                throw new ResourceNotFoundException("post Type ID is null or invalid");
            }
        } catch (Exception e) {
            log.info("Error converting PostDomain to PostDTO", e);
            throw new ResourceNotFoundException("Error converting PostDomain to PostDTO");
            // new ErrorResponse("Error converting PostDomain to PostDTO", e.getMessage());
        }
        return postDto;
    }

    @Override
    protected PostDomain convertDtoToDomain(PostDTO postDto) {
        log.info("Converting PostDTO to PostDomain");

        PostDomain postDomain = new PostDomain();
        try {
            postDomain.setId(postDto.getId());
            postDomain.setTitle(postDto.getTitle());
            postDomain.setContent(postDto.getContent());
            postDomain.setPublicationDate(postDto.getPublicationDate());
            postDomain.setContactNumber(postDto.getContactNumber());
            postDomain.setLocationCoordinates(postDto.getLocationCoordinates());
            postDomain.setSharedCounter(postDto.getSharedCounter());
            postDomain.setStatus(postDto.getStatus());
            postDomain.setIsDeleted(false);

            if (postDto.getIdUser() != null) {
                UserDomain userDomain = userDao.findById(postDto.getIdUser()).orElse(null);
                // verificar si existe
                if (userDomain != null && !userDomain.getIsDeleted()) {
                    postDomain.setUser(userDomain);
                } else {
                    log.info("User not found");
                    throw new ResourceNotFoundException("User not found");
                }

            } else {
                log.info("User not found");
                throw new ResourceNotFoundException("post Type ID is null or invalid");
            }

            if (postDto.getIdPostType() != null) {
                PostTypeDomain postTypeDomain = postTypeDao.findById(postDto.getIdPostType())
                        .orElseThrow(() -> new ResourceNotFoundException("PostType not found"));
                if (postTypeDomain != null && !postTypeDomain.getIsDeleted()) {
                    postDomain.setPostType(postTypeDomain);
                } else {
                    log.info("PostType not found");
                    throw new ResourceNotFoundException("PostType not found");
                }

            } else {
                throw new ResourceNotFoundException("post Type ID is null or invalid");
            }

        } catch (Exception e) {
            log.error("Error converting PostDTO to PostDomain", e);
            throw new ResourceNotFoundException("Error converting PostDTO to PostDomain");
            // new ErrorResponse("Error converting PostDTO to PostDomain", e.getMessage());
        }
        return postDomain;
    }

}
