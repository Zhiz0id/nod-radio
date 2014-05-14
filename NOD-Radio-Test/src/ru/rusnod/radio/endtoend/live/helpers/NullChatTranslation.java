package ru.rusnod.radio.endtoend.live.helpers;

import ru.rusnod.radio.live.chat.ChatTranslation;
import ru.rusnod.radio.live.chat.MessageConsumer;
import ru.rusnod.radio.live.chat.ProgressListener;

public class NullChatTranslation implements ChatTranslation, ChatTranslation.Factory {
    @Override
    public void setProgressListener(ProgressListener listener) {
    }

    @Override
    public void setMessageConsumer(MessageConsumer consumer) {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public ChatTranslation create() {
        return this;
    }
}
