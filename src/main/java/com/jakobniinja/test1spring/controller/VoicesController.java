package com.jakobniinja.test1spring.controller;

import com.jakobniinja.test1spring.exception.SynthesisException;
import com.jakobniinja.test1spring.service.AzureTTSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoicesController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoicesController.class);

    @Autowired
    private AzureTTSService azureTTSService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/voice/sara")
    public void hiFromSara() {

        azureTTSService.setVoiceName("Microsoft Server Speech Text to Speech Voice (en-US, SaraNeural)");

        String text = "hi I'm Sara, sara is a high quality text to speech engine";
        try {
            azureTTSService.synthesizeAndSpeak(text);
        } catch (SynthesisException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/voice/ryan")
    public void hiFromRyan() {

        azureTTSService.setVoiceName("Microsoft Server Speech Text to Speech Voice (en-GB, RyanNeural)");

        String text = "hi I'm Ryan, Ryan is a high quality text to speech engine";
        try {
            azureTTSService.synthesizeAndSpeak(text);
        } catch (SynthesisException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/voice/tony")
    public void hiFromTony() {

        azureTTSService.setVoiceName("Microsoft Server Speech Text to Speech Voice (en-US, TonyNeural)");

        String text = "hi I'm Tony, Tony is a high quality text to speech engine";
        try {
            azureTTSService.synthesizeAndSpeak(text);
        } catch (SynthesisException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
