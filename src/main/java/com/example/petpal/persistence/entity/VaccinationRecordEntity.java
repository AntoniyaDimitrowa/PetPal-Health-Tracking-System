package com.example.petpal.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vaccination_record")
public class VaccinationRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "vaccination_id", nullable = false)
    private VaccinationEntity vaccination;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity pet;
}
