package com.fiuni.adoptamena.api.service.post;

import com.fiuni.adoptamena.api.dao.post.IPostDao;
import com.fiuni.adoptamena.api.dao.post.IPostReportDao;
import com.fiuni.adoptamena.api.dao.post.IReportReasonsDao;
import com.fiuni.adoptamena.api.dao.user.IUserDao;
import com.fiuni.adoptamena.api.domain.post.PostDomain;
import com.fiuni.adoptamena.api.domain.post.PostReportDomain;
import com.fiuni.adoptamena.api.domain.post.ReportReasonsDomain;
import com.fiuni.adoptamena.api.domain.user.UserDomain;
import com.fiuni.adoptamena.api.dto.post.PostReportDto;
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
public class PostReportServiceImpl extends BaseServiceImpl<PostReportDomain, PostReportDto> implements IpostReportService {
    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private IPostReportDao postReportDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IPostDao postDao;

    @Autowired
    private IReportReasonsDao reportReasonsDao;

    @Override
    protected PostReportDto convertDomainToDto(PostReportDomain postReportDomain) {
        log.info("Converting PostReportDomain to PostReportDto");

        PostReportDto postReportDto = new PostReportDto();
        try {
            postReportDto.setId(postReportDomain.getId());
            postReportDto.setDescription(postReportDomain.getDescription());
            postReportDto.setReportDate(postReportDomain.getReportDate());
            postReportDto.setStatus(postReportDomain.getStatus());

            if (postReportDomain.getReportReasons() != null && postReportDomain.getReportReasons().getId() != null) {
                postReportDto.setIdReportReason(postReportDomain.getReportReasons().getId());
            } else {
                log.info("Report Reasons not found");
                throw new ResourceNotFoundException("Report Reasons not found");
            }

            if (postReportDomain.getPost() != null && postReportDomain.getPost().getId() != null) {
                postReportDto.setIdPost(postReportDomain.getPost().getId());
            } else {
                log.info("Post not found");
                throw new ResourceNotFoundException("Post not found");
            }

            if (postReportDomain.getUser() != null && postReportDomain.getUser().getId() != null) {
                postReportDto.setIdUser(postReportDomain.getUser().getId());
            } else {
                log.info("User not found");
                throw new ResourceNotFoundException("User not found");
            }
        } catch (Exception e){
            log.info("Error converting PostReportDomain to PostReportDto");
            throw new ResourceNotFoundException("Error converting PostReportDomain to PostReportDto");
        }
        return postReportDto;
    }

    @Override
    protected PostReportDomain convertDtoToDomain(PostReportDto postReportDto) {
        log.info("Convert PostReportDto to PostReportDomain");

        PostReportDomain postReportDomain = new PostReportDomain();
        try {
            postReportDomain.setId(postReportDto.getId());
            postReportDomain.setDescription(postReportDto.getDescription());
            postReportDomain.setReportDate(postReportDto.getReportDate());
            postReportDomain.setStatus(postReportDto.getStatus());
            postReportDomain.setIsDeleted(false);

            if (postReportDto.getIdUser() != null){
                UserDomain userDomain = userDao.findById(postReportDto.getIdUser()).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
                if (userDomain != null && !userDomain.getIsDeleted()){
                    postReportDomain.setUser(userDomain);
                } else {
                    log.info("User not found");
                    throw new ResourceNotFoundException("User not found");
                }
            } else {
                log.info("User not found");
                throw new ResourceNotFoundException("User ID is null or invalid");
            }

            if (postReportDto.getIdPost() != null) {
                PostDomain postDomain = postDao.findById(postReportDto.getIdPost())
                        .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
                if (postDomain != null && !postDomain.getIsDeleted()) {
                    postReportDomain.setPost(postDomain);
                } else {
                    log.info("Post not found");
                    throw new ResourceNotFoundException("Post not found");
                }

            } else {
                throw new ResourceNotFoundException("Post ID is null or invalid");
            }

            if (postReportDto.getIdReportReason() != null) {
                ReportReasonsDomain reportReasonsDomain = reportReasonsDao.findById(postReportDto.getIdReportReason())
                        .orElseThrow(() -> new ResourceNotFoundException("Report Reason not found"));
                if (reportReasonsDomain != null && !reportReasonsDomain.getIsDeleted()) {
                    postReportDomain.setReportReasons(reportReasonsDomain);
                } else {
                    log.info("Report Reason not found");
                    throw new ResourceNotFoundException("Report Reason not found");
                }

            } else {
                throw new ResourceNotFoundException("Report Reason ID is null or invalid");
            }
        } catch (Exception e) {
            log.error("Error converting PostReportDto to PostReportDomain");
            throw new ResourceNotFoundException("Error converting PostReportDto to PostReportDomain");
        }
        return postReportDomain;
    }

    @Override
    public PostReportDto getById(Integer id) {
        log.info("Getting PostReport by id");
        PostReportDto postReportDto = null;
        try {
            Optional<PostReportDomain> postReportDomainOptional = postReportDao.findById(id);
            if (postReportDomainOptional.isPresent() && !postReportDomainOptional.get().getIsDeleted()) {
                PostReportDomain postReportDomain = postReportDomainOptional.get();
                postReportDto = convertDomainToDto(postReportDomain);
                log.info("Getting PostReport successful");
            }
        } catch (Exception e) {
            log.info("Getting PostReport failed");
            throw new ResourceNotFoundException("Getting PostReport failed");
        }
        return postReportDto;
    }

    @Override
    public List<PostReportDto> getAll(Pageable pageable) {
        return List.of();
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting Post Report");
        try {
            PostReportDomain postReportDomain = postReportDao.findById(id).orElse(null);
            if (postReportDomain != null && !postReportDomain.getIsDeleted()) {
                //postReportDao.delete(postReportDomain); // descomentar para borrar en la base de datos
                postReportDomain.setIsDeleted(true);      // comentar para borrar en la base de datos
                postReportDao.save(postReportDomain);     // comentar para borrar en la base de datos
                log.info("Post Report deleted successful");
            }
        } catch (Exception e) {
            log.info("Error deleting Post Report");
            throw new ResourceNotFoundException("Error deleting Post Report");
        }
    }

    @Override
    public PostReportDto create(PostReportDto postReportDto) {
        PostReportDto savedPostReportDto = null;
        try {
            PostReportDomain postReportDomain = convertDtoToDomain(postReportDto);
            PostReportDomain savedPostReportDomain = postReportDao.save(postReportDomain);

            log.info("Post report saved successful");

            savedPostReportDto = convertDomainToDto(savedPostReportDomain);

        } catch (Exception e) {
            log.info("Post report save failed");
            throw new ResourceNotFoundException("Post report could not be saved");
        }
        return savedPostReportDto;
    }

    @Override
    public PostReportDto update(PostReportDto postReportDto) {
        PostReportDomain savedPostReportDomain = null;
        try {
            PostReportDomain postReportDomain = postReportDao.findById(postReportDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Post report not found"));

            PostReportDomain updatedPostReportDomain = convertDtoToDomain(postReportDto);

            if(!postReportDomain.getIsDeleted()){
                updatedPostReportDomain.setReportDate(postReportDomain.getReportDate());
                updatedPostReportDomain.setIsDeleted(false);
                updatedPostReportDomain.setPost(postReportDomain.getPost());
                updatedPostReportDomain.setUser(postReportDomain.getUser());
                updatedPostReportDomain.setStatus(postReportDomain.getStatus());
            }

            savedPostReportDomain = postReportDao.save(updatedPostReportDomain);
            log.info("Post report updated successful");
        }catch (Exception e){
            log.info("Post report update failed");
            throw new ResourceNotFoundException("Post report update failed");
        }
        return convertDomainToDto(savedPostReportDomain);
    }

    @Override
    public List<PostReportDto> getAllPostsReports(Pageable pageable, Integer userId, Integer postId, Integer reportReasonsId, String description) {
        log.info("Getting all Posts Reports");

        if (userId != null || postId != null || reportReasonsId != null || description != null) {
            Page<PostReportDomain> postReportPage = postReportDao.findByFiltersAndIsDeletedFalse(pageable, userId, postId, reportReasonsId, description);
            return convertDomainListToDtoList(postReportPage.getContent());
        }

        Page<PostReportDomain> postReportPage = postReportDao.findAllByIsDeletedFalse(pageable);
        return convertDomainListToDtoList(postReportPage.getContent());
    }
}
