package com.jakobniinja.test1spring.controller;

import com.jakobniinja.test1spring.exception.SynthesisException;
import com.jakobniinja.test1spring.service.AzureTTSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadTextController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadTextController.class);
    @Autowired
    private AzureTTSService azureTTSService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/{freeText}")
    public void readFreeText(@PathVariable("freeText") String text) {

        try {
            azureTTSService.synthesizeAndSpeak(text);
        } catch (SynthesisException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
