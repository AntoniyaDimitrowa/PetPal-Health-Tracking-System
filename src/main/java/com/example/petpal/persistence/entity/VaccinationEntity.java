package com.example.petpal.persistence.entity;

import com.example.petpal.business.domain.enums.VaccinationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vaccination")
public class VaccinationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VaccinationType type;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private int range;
}
