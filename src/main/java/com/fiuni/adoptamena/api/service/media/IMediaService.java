package com.fiuni.adoptamena.api.service.media;

import com.fiuni.adoptamena.api.service.base.IBaseService;

import org.springframework.web.multipart.MultipartFile;

import com.fiuni.adoptamena.api.domain.media.MediaDomain;
import com.fiuni.adoptamena.api.dto.media.MediaDTO;


public interface IMediaService extends IBaseService<MediaDomain, MediaDTO> {

    public MediaDTO uploadMedia(MultipartFile  file);
    
}
