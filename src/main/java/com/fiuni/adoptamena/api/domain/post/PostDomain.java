package com.fiuni.adoptamena.api.domain.post;

import com.fiuni.adoptamena.api.domain.base.BaseDomain;
import com.fiuni.adoptamena.api.domain.user.UserDomain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Date;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PostDomain implements BaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_post", nullable = false, unique = true)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserDomain user;

    @Column(name = "str_title", nullable = false)
    private String title;

    @Column(name = "str_content")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_publication_date", nullable = false)
    private Date publicationDate;

    @ManyToOne
    @JoinColumn(name = "id_post_type", nullable = false)
    private PostTypeDomain postType;

    @Column(name = "str_location_coordinates")
    private String locationCoordinates;

    @Column(name = "str_contact_number")
    private String contactNumber;

    @Column(name = "str_status", nullable = false)
    private String status;

    @Column(name = "boolean_deleted")
    private Boolean isDeleted;

    @Column(name = "int_shared_counter", nullable = false)
    private Integer sharedCounter;

}
