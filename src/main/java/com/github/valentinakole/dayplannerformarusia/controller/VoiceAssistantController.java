package com.github.valentinakole.dayplannerformarusia.controller;

import com.github.valentinakole.dayplannerformarusia.service.VoiceAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VoiceAssistantController {
    private final VoiceAssistantService voiceAssistantService;

    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity createStat(@RequestBody Object object) {
        return voiceAssistantService.getResponse(object);
    }
}
