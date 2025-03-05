package com.fiuni.adoptamena.api.dao.user;

import com.fiuni.adoptamena.api.domain.user.PasswordResetTokenDomain;
import com.fiuni.adoptamena.api.domain.user.UserDomain;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IPasswordResetTokenDao extends CrudRepository<PasswordResetTokenDomain, Integer> {
    Optional<PasswordResetTokenDomain> findByToken(String token);

    // FindByUser
    PasswordResetTokenDomain findByUser(UserDomain user);
}