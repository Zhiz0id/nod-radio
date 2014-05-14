package ru.rusnod.radio.live.chat.http;

import ru.rusnod.radio.common.ui.Announcer;
import ru.rusnod.radio.live.chat.ChatTranslation;
import ru.rusnod.radio.live.chat.Message;
import ru.rusnod.radio.live.chat.MessageConsumer;
import ru.rusnod.radio.live.chat.ProgressListener;
import ru.rusnod.radio.live.schedule.Scheduler;

import java.util.List;

public class HttpTranslationEngine implements ChatTranslation, HttpChatRequest.ErrorListener, MessageConsumer, Scheduler.Performer {
    private final HttpChatClient chatClient;
    private Announcer<ProgressListener> progressAnnouncer = new Announcer<ProgressListener>(ProgressListener.class);
    private Announcer<MessageConsumer> messageAnnouncer = new Announcer<MessageConsumer>(MessageConsumer.class);
    private final Scheduler pollScheduler;
    private HttpTranslationState currentState;

    public HttpTranslationEngine(String baseUrl, Scheduler refreshScheduler) {
        this(HttpChatClient.create(baseUrl), refreshScheduler);
    }

    public HttpTranslationEngine(HttpChatClient chatClient, Scheduler pollScheduler) {
        this.chatClient = chatClient;
        this.pollScheduler = pollScheduler;
        this.currentState = new HttpTranslationState.Disconnected(this);

        pollScheduler.setPerformer(this);
    }

    @Override
    public void setProgressListener(ProgressListener listener) {
        progressAnnouncer.setTarget(listener);
    }

    @Override
    public void setMessageConsumer(MessageConsumer consumer) {
        messageAnnouncer.setTarget(consumer);
    }

    @Override
    public void start() {
        currentState.onStart();
    }

    @Override
    public void stop() {
        currentState.onStop();
    }

    @Override
    public void shutdown() {
        setMessageConsumer(null);
        setProgressListener(null);

        chatClient.shutdown();
        disconnect();
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
        requestMessages();
    }

    public String currentState() {
        return currentState.getClass().getSimpleName();
    }

    public void disconnect() {
        setCurrentState(new HttpTranslationState.Disconnected(this));
    }

    private void requestMessages() {
        new HttpChatRequest(chatClient, this, this).execute();
    }

    private void setCurrentState(HttpTranslationState newState) {
        this.currentState = newState;
    }

    public void startConnecting() {
        switchToConnecting();
        requestMessages();
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
        scheduleNextPoll();
    }

    public void pauseListening() {
        setCurrentState(new HttpTranslationState.PausedListening(this));
        pollScheduler.cancel();
    }

    public void resumeListening() {
        setCurrentState(new HttpTranslationState.Listening(this));
        scheduleNextPoll();
    }

    private void scheduleNextPoll() {
        pollScheduler.cancel();
        pollScheduler.scheduleNext();
    }
}
