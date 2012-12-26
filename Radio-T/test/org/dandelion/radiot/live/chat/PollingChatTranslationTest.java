package org.dandelion.radiot.live.chat;

import org.dandelion.radiot.live.schedule.DeterministicScheduler;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PollingChatTranslationTest {
    private ChatTranslation realTranslation = mock(ChatTranslation.class);
    private DeterministicScheduler scheduler = new DeterministicScheduler();
    private PollingChatTranslation pollingTranslation = new PollingChatTranslation(realTranslation, scheduler);
    private MessageConsumer consumer = new FakeMessageConsumer();

    @Test
    public void startTranslation_DelegatesToRealTranslation() throws Exception {
        pollingTranslation.start(consumer);
        verify(realTranslation).start(consumer);
    }

    @Test
    public void startTranslation_SchedulesRefresh() throws Exception {
        pollingTranslation.start(consumer);
        scheduler.performAction();
        verify(realTranslation).refresh();
    }

    private static class FakeMessageConsumer implements MessageConsumer {
        @Override
        public void initWithMessages(List<Message> messages) {
        }

        @Override
        public void appendMessages(List<Message> messages) {
        }
    }
}