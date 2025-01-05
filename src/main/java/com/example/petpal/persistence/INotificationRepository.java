package com.example.petpal.persistence;

import com.example.petpal.business.domain.HealthAnalysisResult;
import com.example.petpal.business.domain.User;

public interface INotificationRepository {
    void saveNotification(HealthAnalysisResult result, User user);

}
