package com.jakobniinja.test1spring.controller;

import com.jakobniinja.test1spring.exception.SynthesisException;
import com.jakobniinja.test1spring.service.AzureTTSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEvent.class);

    @Autowired
    private AzureTTSService azureTTSService;


    @RequestMapping(method = RequestMethod.GET, value = "/api/javainuse/sara")
    public void hiFromSara() {

        String text = "hi Sara, I'm a high quality text to speech engine developed by microsoft. It will be my pleasure to serve your needs";
        try {
            azureTTSService.synthesizeAndSpeak(text);
        } catch (SynthesisException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/javainuse/{name}")
    public void sayWelcome(@PathVariable("name") String name) {

        String text = "Hello my friend " + name + ". It's a pleasure to meet you";
        try {
            azureTTSService.synthesizeAndSpeak(text);
        } catch (SynthesisException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
