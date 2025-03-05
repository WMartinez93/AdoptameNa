package com.fiuni.adoptamena.api.service.pet;

import com.fiuni.adoptamena.api.domain.pet.PetDomain;
import com.fiuni.adoptamena.api.dto.pet.PetDTO;
import com.fiuni.adoptamena.api.service.base.IBaseService;
import java.util.List;
public interface IPetService extends IBaseService<PetDomain, PetDTO> {
    public List <PetDTO> getPetByUserId(Integer userId);
}
