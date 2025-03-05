package com.fiuni.adoptamena.api.domain.user;

import java.io.Serial;
import java.util.Date;
import java.util.UUID;

import com.fiuni.adoptamena.api.domain.base.BaseDomain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "password_reset_tokens")
@Data
@NoArgsConstructor
public class PasswordResetTokenDomain implements BaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String token;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserDomain user;

    @Column(nullable = false)
    private Date expiryDate;

    public PasswordResetTokenDomain(UserDomain user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.expiryDate = new Date(System.currentTimeMillis() + 3600 * 1000); // Expira en 1 hora
    }

    public boolean isExpired() {
        return new Date().after(expiryDate);
    }
}
