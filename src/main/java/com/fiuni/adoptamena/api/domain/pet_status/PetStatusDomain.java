package com.fiuni.adoptamena.api.domain.pet_status;

import com.fiuni.adoptamena.api.domain.base.BaseDomain;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serial;

@Entity
@Table(name = "pet_statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetStatusDomain implements BaseDomain {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pet_status", nullable = false, unique = true)
    private Integer id;

    @Column(name = "str_name", length = 100)
    private String name;

    @Column(name = "str_description", length = 500)
    private String description;

    @Column(name = "bool_deleted")
    private Boolean isDeleted;

}
