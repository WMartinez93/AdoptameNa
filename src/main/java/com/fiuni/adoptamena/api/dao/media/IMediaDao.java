package com.fiuni.adoptamena.api.dao.media;

import org.springframework.data.repository.CrudRepository;

import com.fiuni.adoptamena.api.domain.media.MediaDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMediaDao extends CrudRepository<MediaDomain, Integer> {
    Page<MediaDomain> findAll(Pageable pageable);
    
}
