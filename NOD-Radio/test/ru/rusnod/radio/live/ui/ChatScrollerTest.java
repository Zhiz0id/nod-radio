package ru.rusnod.radio.live.ui;

import org.junit.Test;
import ru.rusnod.radio.live.chat.Message;
import ru.rusnod.radio.live.chat.MessageConsumer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ChatScrollerTest {
    public static final List<Message> MESSAGES = Arrays.asList(new Message("", "", "", 0));
    private final MessageConsumer consumer = mock(MessageConsumer.class);
    private final ChatStreamView chatView = mock(ChatStreamView.class);
    private final ChatScroller scroller = new ChatScroller(consumer, chatView);

    @Test
    public void appendMessages_DelegatesToConsumer() throws Exception {
        scroller.processMessages(MESSAGES);
        verify(consumer).processMessages(MESSAGES);
    }

    @Test
    public void whenAtBottomOfList_ScrollsDownAfterAppending() throws Exception {
        when(chatView.atBottom()).thenReturn(true);

        scroller.processMessages(MESSAGES);
        verify(chatView).scrollToBottom();
    }

    @Test
    public void whenNotAtBottomOfList_KeepScrollPositionUnchanged() throws Exception {
        when(chatView.atBottom()).thenReturn(false);
        scroller.processMessages(MESSAGES);
        verify(chatView, never()).scrollToBottom();
    }
}
