package com.github.valentinakole.dayplannerformarusia.service;

import org.springframework.http.ResponseEntity;

public interface VoiceAssistantService {
    ResponseEntity getResponse(Object object);
}