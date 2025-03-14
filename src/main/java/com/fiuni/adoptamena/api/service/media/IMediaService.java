package com.fiuni.adoptamena.api.service.media;

import com.fiuni.adoptamena.api.service.base.IBaseService;

import org.springframework.web.multipart.MultipartFile;

import com.fiuni.adoptamena.api.domain.media.MediaDomain;
import com.fiuni.adoptamena.api.dto.media.ResponseMediaDTO;


public interface IMediaService extends IBaseService<MediaDomain, ResponseMediaDTO> {

    public ResponseMediaDTO uploadMedia(MultipartFile  file);
    
}
