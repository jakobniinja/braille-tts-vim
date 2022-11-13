package com.jakobniinja.test1spring.service;

import com.jakobniinja.test1spring.exception.SynthesisException;
import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Service class providing 2 text-to-speech services
 */
@Service
public class AzureTTSService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureTTSService.class);

    @Value("${azure.cognitive-service.api-key}")
    private String key;

    @Value("${azure.region}")
    private String region;

    @Value("${azure.synthesis.language}")
    private String language;

    @Value("${azure.synthesis.voice-name}")
    private String voiceName;

    @Value("${azure.synthesis.output-format-for-speaker}")
    private String outputFormatForSpeaker;

    @Value("${azure.synthesis.output-format-for-file}")
    private String outputFormatForFile;

    @Value("${azure.synthesis.output-folder}")
    private String outputFolder;

    private SpeechConfig speechConfig;

    /**
     * Initialize SpeechConfig with API Key, Azure region, language, and voice name
     */
    private void initSpeechConfig() {
        this.speechConfig = SpeechConfig.fromSubscription(key, region);
        speechConfig.setSpeechSynthesisLanguage(language);
        speechConfig.setSpeechSynthesisVoiceName(voiceName);
    }

    /**
     * Check if synthesis was successful
     *
     * @param result the result of the synthesis
     */
    private boolean isSynthesisSuccessful(SpeechSynthesisResult result) {
        if (result == null) {
            return false;
        }
        if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
            LOGGER.info("Speech synthesized!");
        } else if (result.getReason() == ResultReason.Canceled) {
            SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(result);
            LOGGER.error("Speech synthesis cancelled! Reason: {}", cancellation.getReason());

            if (cancellation.getReason() == CancellationReason.Error) {
                LOGGER.error("Speech synthesis cancelled! Error code: {}", cancellation.getErrorCode());
                LOGGER.error("Speech synthesis cancelled! Error details: {}", cancellation.getErrorDetails());
            }
            return false;
        }
        return true;
    }

    /**
     * Synthesize text and save speech to a .wav file
     *
     * @param text text to synthesize
     */
    public void synthesizeAndSaveToFile(String text) throws SynthesisException {
        String audioFileAbsolutePath = outputFolder + UUID.randomUUID() + ".wav";
        initSpeechConfig();
        speechConfig.setSpeechSynthesisOutputFormat(SpeechSynthesisOutputFormat.valueOf(outputFormatForFile));
        AudioConfig audioConfig = AudioConfig.fromWavFileOutput(audioFileAbsolutePath);
        SpeechSynthesizer synth = new SpeechSynthesizer(this.speechConfig, audioConfig);
        Future<SpeechSynthesisResult> task = synth.SpeakTextAsync(text);
        SpeechSynthesisResult result = null;
        try {
            result = task.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new SynthesisException(e.getMessage());
        }
        synth.close();
        speechConfig.close();
        File audioFile = new File(audioFileAbsolutePath);
        if (isSynthesisSuccessful(result)) {
            long fileSize = 0;
            try {
                fileSize = Files.size(Paths.get(audioFile.getAbsolutePath()));
            } catch (IOException e) {
                throw new SynthesisException(e.getMessage());
            }
            if (fileSize > 0) {
                LOGGER.info("Audio file of {} bytes generated at {}", fileSize, audioFile.getAbsolutePath());
            }

        } else {
            LOGGER.error("Audio file was generated empty");
            try {
                if (Files.deleteIfExists(Paths.get(audioFile.getAbsolutePath()))) {
                    LOGGER.info("Audio file deleted successfully");
                } else {
                    LOGGER.info("Failed to delete audio file");
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            throw new SynthesisException("Synthesis failed!");
        }
    }

    /**
     * Synthesize text to speaker output
     *
     * @param text text to synthesize
     */
    public void synthesizeAndSpeak(String text) throws SynthesisException {
        initSpeechConfig();
        this.speechConfig.setSpeechSynthesisOutputFormat(SpeechSynthesisOutputFormat.valueOf(outputFormatForSpeaker));
        SpeechSynthesizer synth = new SpeechSynthesizer(speechConfig);
        Future<SpeechSynthesisResult> task = synth.SpeakTextAsync(text);
        SpeechSynthesisResult result = null;
        try {
            result = task.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new SynthesisException(e.getMessage());
        }
        synth.close();
        speechConfig.close();
        if (!isSynthesisSuccessful(result)) {
            throw new SynthesisException("Synthesis failed!");
        }
    }
}
