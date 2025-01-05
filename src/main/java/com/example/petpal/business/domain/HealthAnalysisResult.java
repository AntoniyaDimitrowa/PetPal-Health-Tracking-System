package com.example.petpal.business.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class HealthAnalysisResult {
    private Long resultId;
    private Date date;
    private Pet pet;
    private String message;
    private boolean isRead;
}
