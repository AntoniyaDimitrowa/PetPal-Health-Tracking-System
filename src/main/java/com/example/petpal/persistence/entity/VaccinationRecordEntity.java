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
    @JoinColumn(name = "vaccination_id")
    private VaccinationEntity vaccination;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private PetEntity pet;
}
