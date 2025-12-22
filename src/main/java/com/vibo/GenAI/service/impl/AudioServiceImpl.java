package com.vibo.GenAI.service.impl;

import com.vibo.GenAI.service.AudioService;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AudioServiceImpl implements AudioService {

    private final OpenAiAudioTranscriptionModel audioTranscriptionModel;
    private final OpenAiAudioSpeechModel aiAudioSpeechModel;

    public AudioServiceImpl(OpenAiAudioTranscriptionModel audioTranscriptionModel, OpenAiAudioSpeechModel aiAudioSpeechModel) {
        this.audioTranscriptionModel = audioTranscriptionModel;
        this.aiAudioSpeechModel = aiAudioSpeechModel;
    }

    @Override
    public String audioToText(MultipartFile file) {

        OpenAiAudioTranscriptionOptions options = new OpenAiAudioTranscriptionOptions.Builder()
                .language("ta")
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.SRT) // helps to provide timestamp
                .build();

        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(file.getResource(),options);
        return audioTranscriptionModel.call(prompt).getResult().getOutput();

    }

    @Override
    public byte[] textToAudio(String text) {
        OpenAiAudioSpeechOptions options = new OpenAiAudioSpeechOptions.Builder()
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ECHO)
                .speed(2.0)
                .build();
        TextToSpeechPrompt prompt = new TextToSpeechPrompt(text,options);
        return aiAudioSpeechModel.call(prompt).getResult().getOutput();
    }
}
