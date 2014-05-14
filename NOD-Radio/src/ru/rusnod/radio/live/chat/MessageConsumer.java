package ru.rusnod.radio.live.chat;

import java.util.List;

public interface MessageConsumer {
    void processMessages(List<Message> messages);
}
