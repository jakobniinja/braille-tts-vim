package com.jakobniinja.test1spring;


import com.jakobniinja.test1spring.exception.SynthesisException;
import com.jakobniinja.test1spring.service.AzureTTSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyListener.class);
    @Value("${server.port}")
    private int serverPort;
    @Autowired
    private AzureTTSService azureTTSService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String text = "Server is now up and running on port " + serverPort + ". And swagger ui is also configured with swagger v3";
        try {
            azureTTSService.synthesizeAndSpeak(text);
        } catch (SynthesisException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
