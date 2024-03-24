package com.github.valentina810.weekplannerformarusia.controller;

import com.github.valentina810.weekplannerformarusia.service.WeekPlannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping(path = "/webhook")
public class WeekPlannerController {
    private final WeekPlannerService weekPlannerService;

    @PostMapping
    @CrossOrigin(origins = {"https://skill-debugger.marusia.mail.ru"})
    public ResponseEntity<?> createResponse(@RequestBody Object object) {
        log.info("---------------------------------------------------");
        log.info("Тело входного запроса {}", object.toString());
        return weekPlannerService.getResponse(object);
    }
}
