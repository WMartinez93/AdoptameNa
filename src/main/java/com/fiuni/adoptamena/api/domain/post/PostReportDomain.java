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
@Table(name = "post_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostReportDomain implements BaseDomain {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_post_report", nullable = false, unique = true)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserDomain user;

    @ManyToOne
    @JoinColumn(name = "id_post")
    private PostDomain post;

    @ManyToOne
    @JoinColumn(name = "id_reason")
    private ReportReasonsDomain reportReasons;

    @Column(name = "str_description")
    private String description;

    @Column(name = "date_report_date")
    private Date reportDate;

    @Column(name = "str_status")
    private String status;

    @Column(name = "boolean_deleted")
    private Boolean isDeleted;

}
