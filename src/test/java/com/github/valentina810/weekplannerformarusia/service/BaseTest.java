package com.github.valentina810.weekplannerformarusia.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.function.BiFunction;

@SpringBootTest
public class BaseTest {

    @Autowired
    protected WeekPlannerService weekPlannerService;

    protected BiFunction<JSONObject, String, JSONObject> getStateValue = (jsonObject, s) ->
            Optional.ofNullable(jsonObject.optJSONObject("state"))
                    .orElse(new JSONObject()).optJSONObject(s);
}