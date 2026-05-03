package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.ResetPasswordRequest;
import com.inmap.restfulApiInMap.error.NotFoundException;

public interface EmailService {
    public void sendTextEmail(String to, String subject, String body);
    public void requestPasswordReset(String data) throws NotFoundException;
    public void newPasswordReset(ResetPasswordRequest reset) throws NotFoundException;
}
