package com.fiuni.adoptamena.api.domain.media;

import com.fiuni.adoptamena.api.domain.base.BaseDomain;
import com.fiuni.adoptamena.api.domain.user.UserDomain;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serial;
import java.util.Date;

@Entity
@Table(name = "media")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDomain implements BaseDomain {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_media", nullable = false, unique = true)
    private Integer id;

    @Column(name = "str_mime_type", nullable = false)
    private String mimeType; // Tipo MIME (ej. "image/png", "video/mp4")

    @Column(name = "str_url", nullable = false)
    private String url; // URL relativa del archivo

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UserDomain user;

    @Column(name = "date_upload_date", nullable = false)
    private Date uploadDate;
}