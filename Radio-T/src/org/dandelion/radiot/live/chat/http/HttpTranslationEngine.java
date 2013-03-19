package org.dandelion.radiot.live.chat.http;

import android.util.Log;
import org.dandelion.radiot.common.ui.Announcer;
import org.dandelion.radiot.live.chat.Message;
import org.dandelion.radiot.live.chat.MessageConsumer;
import org.dandelion.radiot.live.chat.ProgressListener;
import org.dandelion.radiot.live.schedule.Scheduler;

import java.util.List;

public class HttpTranslationEngine implements HttpChatRequest.ErrorListener, MessageConsumer, Scheduler.Performer {
    private final HttpChatClient chatClient;
    private Announcer<ProgressListener> progressAnnouncer = new Announcer<ProgressListener>(ProgressListener.class);
    private Announcer<MessageConsumer> messageAnnouncer = new Announcer<MessageConsumer>(MessageConsumer.class);
    private final Scheduler pollScheduler;
    private HttpTranslationState currentState;

    public HttpTranslationEngine(HttpChatClient chatClient, Scheduler pollScheduler) {
        this.chatClient = chatClient;
        this.pollScheduler = pollScheduler;
        pollScheduler.setPerformer(this);
        this.currentState = new HttpTranslationState.Disconnected(this);
    }

    public void setProgressListener(ProgressListener listener) {
        progressAnnouncer.setTarget(listener);
    }

    public void setMessageConsumer(MessageConsumer consumer) {
        messageAnnouncer.setTarget(consumer);
    }

    public void disconnect() {
        setCurrentState(new HttpTranslationState.Disconnected(this));
    }

    public HttpTranslationState currentState() {
        return currentState;
    }

    private void requestMessages(String mode) {
        new HttpChatRequest(mode, chatClient, this, this).execute();
    }

    @Override
    public void onError() {
        progressAnnouncer.announce().onError();
        currentState.onError();
    }

    @Override
    public void processMessages(List<Message> messages) {
        currentState.onRequestCompleted();
        messageAnnouncer.announce().processMessages(messages);
    }

    @Override
    public void performAction() {
        requestMessages("next");
    }

    public void setCurrentState(HttpTranslationState newState) {
        Log.d("CHAT", "Changing state " + this.currentState.getClass().getSimpleName() + " -> " + newState.getClass().getSimpleName());
        this.currentState = newState;
    }

    public void startConnecting() {
        switchToConnecting();
        requestMessages("last");
    }

    private void switchToConnecting() {
        progressAnnouncer.announce().onConnecting();
        setCurrentState(new HttpTranslationState.Connecting(this, progressAnnouncer.announce()));
    }

    public void pauseConnecting() {
        setCurrentState(new HttpTranslationState.PausedConnecting(this, progressAnnouncer.announce()));
    }

    public void resumeConnecting() {
        switchToConnecting();
    }

    public void startListening() {
        setCurrentState(new HttpTranslationState.Listening(this));
        pollScheduler.scheduleNext();
    }

    public void pauseListening() {
        setCurrentState(new HttpTranslationState.PausedListening(this));
        pollScheduler.cancel();
    }

    public void resumeListening() {
        setCurrentState(new HttpTranslationState.Listening(this));
        pollScheduler.scheduleNext();
    }

    public void shutdown() {
        setMessageConsumer(null);
        setProgressListener(null);

        chatClient.shutdown();
        disconnect();
    }
}
