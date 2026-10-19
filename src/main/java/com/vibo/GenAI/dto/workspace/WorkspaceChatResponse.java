package com.vibo.GenAI.dto.workspace;

import java.util.List;

public class WorkspaceChatResponse {
    private String answer;
    private String modeUsed;
    private List<SourceMetadata> sources;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getModeUsed() {
        return modeUsed;
    }

    public void setModeUsed(String modeUsed) {
        this.modeUsed = modeUsed;
    }

    public List<SourceMetadata> getSources() {
        return sources;
    }

    public void setSources(List<SourceMetadata> sources) {
        this.sources = sources;
    }
}
