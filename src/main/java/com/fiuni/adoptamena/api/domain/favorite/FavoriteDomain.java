package com.fiuni.adoptamena.api.domain.favorite;

import com.fiuni.adoptamena.api.domain.base.BaseDomain;
import com.fiuni.adoptamena.api.domain.post.PostDomain;
import com.fiuni.adoptamena.api.domain.user.UserDomain;
import lombok.*;
import jakarta.persistence.*;
import java.io.Serial;

@Entity
@Table(name = "favorites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDomain implements BaseDomain {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorite", nullable = false, unique = true)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_post_id", nullable = false)
    private PostDomain post;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserDomain user;
}