    package com.example.petpal.persistence.entity;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.Min;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "breed_health_info")
    public class BreedHealthInfoEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "breed_id", nullable = false)
        private BreedEntity breed;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        private UserEntity user;

        @NotNull
        @Min(0)
        @Column(nullable = false)
        private int ageRangeStart;

        @NotNull
        @Min(1)
        @Column(nullable = false)
        private int ageRangeEnd;

        @NotNull
        @Column(nullable = false)
        private double normalFoodIntake;  // in grams

        @NotNull
        @Column(nullable = false)
        private double normalWaterIntake; // in liters
    }
