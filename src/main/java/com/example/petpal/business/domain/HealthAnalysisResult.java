package com.example.petpal.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class HealthAnalysisResult {
    private Long resultId;
    private Date date;
    private Pet pet;
    private String message;
    private boolean isRead;
}
