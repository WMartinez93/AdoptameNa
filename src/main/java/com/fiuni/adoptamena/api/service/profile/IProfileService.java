package com.fiuni.adoptamena.api.service.profile;

import com.fiuni.adoptamena.api.dto.profile.ProfileDTO;
import com.fiuni.adoptamena.api.dto.user.UserProfileDTO;
import com.fiuni.adoptamena.api.service.base.IBaseService;
import com.fiuni.adoptamena.api.domain.profile.ProfileDomain;

public interface IProfileService extends IBaseService<ProfileDomain, ProfileDTO> {

    UserProfileDTO getUserProfileDTO(Integer id);


}
