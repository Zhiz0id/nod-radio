package org.dandelion.radiot.live.chat.http;

import android.util.Log;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

import java.util.List;

public class HttpTranslationState {
    protected final HttpTranslationEngine engine;

    public HttpTranslationState(HttpTranslationEngine engine) {
        this.engine = engine;
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void enter() {

    }

    static class Disconnected extends HttpTranslationState {
        public Disconnected(HttpTranslationEngine httpTranslationEngine) {
            super(httpTranslationEngine);
        }

        @Override
        public void onStart() {
            engine.beConnecting();
        }
    }

    public static class Paused extends HttpTranslationState {
        public Paused(HttpTranslationEngine engine) {
            super(engine);
        }

        @Override
        public void onStart() {
            engine.beListening();
        }
    }

    static class Connecting extends HttpTranslationState implements MessageConsumer, HttpChatRequest.ErrorListener {
        private final MessageConsumer consumer;
        private final ProgressListener progressListener;
        private final HttpChatClient chatClient;
        private boolean isStopped = false;

        public Connecting(HttpTranslationEngine engine, HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener) {
            super(engine);
            this.chatClient = chatClient;
            this.progressListener = progressListener;
            this.consumer = consumer;
        }

        @Override
        public void onStart() {
            isStopped = false;
            progressListener.onConnecting();
        }

        @Override
        public void onStop() {
            isStopped = true;
        }

        @Override
        public void enter() {
            progressListener.onConnecting();
            requestMessages();
        }

        private void requestMessages() {
            new HttpChatRequest("last", chatClient, this, this).execute();
        }

        @Override
        public void processMessages(List<Message> messages) {
            if (isStopped) {
                engine.bePaused();
            } else {
                engine.beListening();
            }

            progressListener.onConnected();
            consumer.processMessages(messages);
        }

        @Override
        public void onError() {
            progressListener.onError();
            engine.beDisconnected();
        }
    }


    public static class Listening extends HttpTranslationState implements Scheduler.Performer, MessageConsumer, HttpChatRequest.ErrorListener {
        private final MessageConsumer consumer;
        private final ProgressListener progressListener;
        private final HttpChatClient chatClient;
        private boolean inProgress = false;
        private boolean isStopped = false;

        public Listening(HttpTranslationEngine engine, HttpChatClient chatClient, MessageConsumer consumer, ProgressListener progressListener) {
            super(engine);
            this.chatClient = chatClient;
            this.progressListener = progressListener;
            this.consumer = consumer;
        }

        // TODO: This method should never be called at all
        @Override
        public void onStart() {
            requestMessages();
        }

        private void scheduleUpdate() {
            engine.schedulePoll(this);
        }

        @Override
        public void enter() {
            Log.d("CHAT", "Scheduling update from in " + this.toString());
            scheduleUpdate();
        }

        @Override
        public void onStop() {
            isStopped = true;
            engine.cancelPoll();
            engine.bePaused();
        }

        @Override
        public void performAction() {
            requestMessages();
        }

        public void requestMessages() {
            if (inProgress) return;

            inProgress = true;
            new HttpChatRequest("next", chatClient, this, this).execute();
        }

        @Override
        public void processMessages(List<Message> messages) {
            consumer.processMessages(messages);
            if (isStopped) {
                engine.bePaused();
            } else {
                scheduleUpdate();
            }
            inProgress = false;
        }

        @Override
        public void onError() {
            progressListener.onError();
            inProgress = false;
        }
    }
}
