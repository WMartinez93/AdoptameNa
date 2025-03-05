package com.fiuni.adoptamena.api.domain.breed;

import com.fiuni.adoptamena.api.domain.animal.AnimalDomain;
import com.fiuni.adoptamena.api.domain.base.BaseDomain;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serial;

@Entity
@Table(name = "breeds")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreedDomain implements BaseDomain {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_race", nullable = false, unique = true)
    private Integer id;

    @Column(name = "str_name", length = 100)
    private String name;

    @Column(name = "bool_deleted")
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "id_animal")
    private AnimalDomain animal;
}
