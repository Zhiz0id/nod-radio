package ru.rusnod.radio.endtoend.live;

import ru.rusnod.radio.endtoend.live.helpers.ChatTranslationRunner;
import ru.rusnod.radio.endtoend.live.helpers.LiveChatTranslationServer;
import ru.rusnod.radio.live.chat.ChatTranslation;
import ru.rusnod.radio.live.chat.http.HttpTranslationEngine;
import ru.rusnod.radio.live.schedule.DeterministicScheduler;
import ru.rusnod.radio.live.ui.ChatTranslationFragment;
import ru.rusnod.radio.endtoend.live.helpers.LiveChatTranslationServer;

import static ru.rusnod.radio.util.ChatStreamBuilder.chatStream;
import static ru.rusnod.radio.util.ChatStreamBuilder.message;

public class ChatTranslationTest extends LiveShowActivityTestCase {
    private LiveChatTranslationServer backend;
    private DeterministicScheduler scheduler;

    public void testAtStartup_RequestsChatContent() throws Exception {
        ChatTranslationRunner app = openScreen();
        backend.hasReceivedInitialRequest();
        backend.respondWithChatStream(chatStream(
                message(1, "Lorem ipsum"),
                message(2, "Dolor sit amet")));

        app.showsChatMessages("Lorem ipsum", "Dolor sit amet");
    }

    public void testRequestNextMessagesWhenRefreshing() throws Exception {
        final int INITIAL_SEQ = 1;
        final ChatTranslationRunner app = openScreen();

        backend.hasReceivedInitialRequest();
        backend.respondWithChatStream(chatStream(message(INITIAL_SEQ, "Lorem ipsum")));
        app.showsChatMessages("Lorem ipsum");

        app.refreshChat();
        backend.hasReceivedContinuationRequest(INITIAL_SEQ);
        backend.respondWithChatStream(chatStream(message(INITIAL_SEQ + 1, "Dolor sit amet")));
        app.showsChatMessages(
                "Lorem ipsum",
                "Dolor sit amet");

        app.refreshChat();
        backend.hasReceivedContinuationRequest(INITIAL_SEQ + 1);
        backend.respondWithChatStream(chatStream(message(INITIAL_SEQ + 2, "Consectetur adipiscing elit")));
        app.showsChatMessages(
                "Lorem ipsum",
                "Dolor sit amet",
                "Consectetur adipiscing elit");
    }

    public void testDisplayingErrorWhenUnableToGetMessages() throws Exception {
        ChatTranslationRunner app = openScreen();

        backend.hasReceivedInitialRequest();
        backend.respondWithError();
        app.showsErrorMessage();
    }

    private ChatTranslationRunner openScreen() {
        return new ChatTranslationRunner(getInstrumentation(), getActivity(), scheduler);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        backend = new LiveChatTranslationServer();
        scheduler = new DeterministicScheduler();
        ChatTranslationFragment.chatFactory = new ChatTranslation.Factory() {
            @Override
            public ChatTranslation create() {
                return new HttpTranslationEngine(
                        LiveChatTranslationServer.baseUrl(), scheduler);
            }
        };
    }

    @Override
    public void tearDown() throws Exception {
        backend.stop();
        super.tearDown();
    }
}

