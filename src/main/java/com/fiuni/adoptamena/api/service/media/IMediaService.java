package com.fiuni.adoptamena.api.service.media;

import com.fiuni.adoptamena.api.service.base.IBaseService;
import com.fiuni.adoptamena.api.domain.media.MediaDomain;
import com.fiuni.adoptamena.api.dto.media.RequestMediaDTO;
import com.fiuni.adoptamena.api.dto.media.ResponseMediaDTO;


public interface IMediaService extends IBaseService<MediaDomain, ResponseMediaDTO> {

    public ResponseMediaDTO uploadMedia(RequestMediaDTO media);
    
}
