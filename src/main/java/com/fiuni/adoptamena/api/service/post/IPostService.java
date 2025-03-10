package com.fiuni.adoptamena.api.service.post;

import com.fiuni.adoptamena.api.domain.post.PostDomain;
import com.fiuni.adoptamena.api.dto.post.PostDTO;
import com.fiuni.adoptamena.api.service.base.IBaseService;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPostService extends IBaseService<PostDomain, PostDTO> {



    List<PostDTO> getAllPosts(Pageable pageable, String title, String content, Integer userId, String postTypeName);

    List<PostDTO> searchPostByKeyword(Pageable pageable, String keyword);

    void increaseSharedCounter(Integer postId);

}
