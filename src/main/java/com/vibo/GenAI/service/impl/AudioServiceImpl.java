package com.vibo.GenAI.service.impl;

import com.vibo.GenAI.service.AudioService;
import com.vibo.GenAI.huggingface.HuggingFaceAudioTranscriptionModel;
import com.vibo.GenAI.huggingface.HuggingFaceAudioSpeechModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class AudioServiceImpl implements AudioService {

    private final HuggingFaceAudioTranscriptionModel transcriptionModel;
    private final HuggingFaceAudioSpeechModel speechModel;

    public AudioServiceImpl(HuggingFaceAudioTranscriptionModel transcriptionModel, HuggingFaceAudioSpeechModel speechModel) {
        this.transcriptionModel = transcriptionModel;
        this.speechModel = speechModel;
    }

    @Override
    public String audioToText(MultipartFile file) {
        try {
            byte[] audioBytes = file.getBytes();
            return transcriptionModel.transcribe(audioBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read audio file", e);
        }
    }

    @Override
    public byte[] textToAudio(String text) {
        return speechModel.generateSpeech(text);
    }
}
