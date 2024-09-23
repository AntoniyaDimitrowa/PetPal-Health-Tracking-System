package com.example.petpal.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
public class OwnerEntity extends UserEntity {
    private ArrayList<PetEntity> pets;

}
