package org.dandelion.radiot.live.chat;

import java.util.List;

public interface ChatTranslation {
    public interface MessageConsumer {
        void addMessages(List<String> messages);
    }
    void requestLastRecords(MessageConsumer consumer);
}