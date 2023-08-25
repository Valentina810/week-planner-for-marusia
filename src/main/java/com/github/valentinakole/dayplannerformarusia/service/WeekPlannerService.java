package com.github.valentinakole.dayplannerformarusia.service;

import org.springframework.http.ResponseEntity;

public interface WeekPlannerService {
    ResponseEntity getResponse(Object object);
}