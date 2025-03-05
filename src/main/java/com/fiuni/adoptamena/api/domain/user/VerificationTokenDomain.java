package com.fiuni.adoptamena.api.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "verification_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationTokenDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserDomain user;

    @Column(nullable = false)
    private Date expiryDate;

    public VerificationTokenDomain(UserDomain user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.expiryDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // Expira en 24 horas
    }

    public Boolean isExpired() {
        return new Date().after(expiryDate);
    }
}