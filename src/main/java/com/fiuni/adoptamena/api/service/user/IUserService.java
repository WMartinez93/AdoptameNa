package com.fiuni.adoptamena.api.service.user;

import com.fiuni.adoptamena.api.domain.user.UserDomain;
import com.fiuni.adoptamena.api.dto.user.UserDTO;
import com.fiuni.adoptamena.api.dto.user.UserProfileDTO;
import com.fiuni.adoptamena.api.service.base.IBaseService;

public interface IUserService extends IBaseService<UserDomain, UserDTO> {

    UserProfileDTO getUserProfileDTO(Integer id);

}
