package com.example.petpal.business;

import com.example.petpal.business.domain.HealthAnalysisResult;
import com.example.petpal.business.domain.HealthRecord;
import com.example.petpal.business.exception.InvalidPetException;
import com.example.petpal.business.exception.InvalidUserException;

public interface IPetHealthAnalyzer {
    HealthAnalysisResult analyzeHealthRecord(Long petId, HealthRecord healthRecord) throws InvalidPetException, InvalidUserException;
}
