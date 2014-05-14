package ru.rusnod.radio.live.ui;

import ru.rusnod.radio.live.chat.Message;
import ru.rusnod.radio.live.chat.MessageConsumer;

import java.util.List;

public class ChatScroller implements MessageConsumer {
    private final MessageConsumer consumer;
    private final ChatStreamView view;

    public ChatScroller(MessageConsumer consumer, ChatStreamView view) {
        this.consumer = consumer;
        this.view = view;
    }

    @Override
    public void processMessages(List<Message> messages) {
        boolean willScroll = view.atBottom();
        consumer.processMessages(messages);
        if (willScroll) {
            view.scrollToBottom();
        }
    }
}
