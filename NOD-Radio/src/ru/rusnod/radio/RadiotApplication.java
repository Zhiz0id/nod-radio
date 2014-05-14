package ru.rusnod.radio;

import android.app.Application;
import android.os.Handler;
import ru.rusnod.radio.live.chat.ChatTranslation;
import ru.rusnod.radio.live.chat.http.HttpTranslationEngine;
import ru.rusnod.radio.live.schedule.Scheduler;
import ru.rusnod.radio.live.topics.TopicTrackerFactory;
import ru.rusnod.radio.live.ui.ChatTranslationFragment;
import ru.rusnod.radio.live.ui.topics.CurrentTopicFragment;
import ru.rusnod.radio.podcasts.main.PodcastsApp;
import ru.rusnod.radio.util.ProgrammerError;

public class RadiotApplication extends Application {
    private static final String CHAT_URL = "http://chat.radio-t.com";
    private static final String TOPIC_TRACKER_BASE_URL = "107.170.84.215:8080/chat";
    // private static final String CHAT_URL = "http://192.168.5.206:4567";

    @Override
    public void onCreate() {
        super.onCreate();
        PodcastsApp.initialize(this);
        setupChatTranslation();
        setupTopicTracker();
    }

    private void setupTopicTracker() {
        CurrentTopicFragment.trackerFactory = new TopicTrackerFactory(TOPIC_TRACKER_BASE_URL);
    }

    private void setupChatTranslation() {
        ChatTranslationFragment.chatFactory = new ChatTranslation.Factory() {
            @Override
            public ChatTranslation create() {
                return new HttpTranslationEngine(CHAT_URL, new HandlerScheduler());
            }
        };
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PodcastsApp.release();
    }

    private static class HandlerScheduler implements Scheduler {
        private Handler handler = new Handler();
        private Performer performer;
        private boolean isScheduled = false;
        private Runnable action = new Runnable() {
            @Override
            public void run() {
                performer.performAction();
                isScheduled = false;
            }
        };

        @Override
        public void setPerformer(Performer performer) {
            this.performer = performer;
        }

        @Override
        public void scheduleNext() {
            if (isScheduled) {
                throw new ProgrammerError("The previous action hasn't finished yet");
            }

            isScheduled = true;
            handler.postDelayed(action, 5000);
        }

        @Override
        public void cancel() {
            handler.removeCallbacks(action);
            isScheduled = false;
        }
    }
}
