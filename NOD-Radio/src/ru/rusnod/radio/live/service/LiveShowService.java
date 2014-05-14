package ru.rusnod.radio.live.service;

import android.content.Context;
import android.content.Intent;
import ru.rusnod.radio.R;
import ru.rusnod.radio.live.LiveShowApp;
import ru.rusnod.radio.live.core.*;
import ru.rusnod.radio.live.ui.LiveNotificationManager;
import ru.rusnod.radio.util.IconNote;

public class LiveShowService extends WakefulService implements PlayerActivityListener {
    private static final String TAG = LiveShowService.class.getName();
    public static final String TOGGLE_ACTION = TAG + ".Toggle";
    private static final String TIMEOUT_ACTION = TAG + ".Timeout";

    private LiveShowPlayer player;
    private TimeoutScheduler scheduler;
    private AudioStream stream;
    private LiveShowStateListener statusDisplayer;
    private Lockable networkLock;

    public static void sendTimeoutElapsed(Context context) {
        performWakefulAction(context, LiveShowService.class, TIMEOUT_ACTION);
    }

    public static void sendTogglePlayback(Context context) {
        performWakefulAction(context, LiveShowService.class, TOGGLE_ACTION);
    }

    public static Intent createToggleIntent(Context context) {
        return createIntent(context, LiveShowService.class, TOGGLE_ACTION);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createInfrastructure();
        createPlayer();
        createVisual();
    }

    private void createInfrastructure() {
        networkLock = app().createNetworkLock(this);
        scheduler = new TimeoutScheduler(new AlarmTimeout(this, TimeoutReceiver.BROADCAST));
        stream = app().createAudioStream();
    }

    private void createPlayer() {
        player = new LiveShowPlayer(stream, stateHolder(), scheduler);
        scheduler.setPerformer(player);
        player.setActivityListener(this);
    }

    private void createVisual() {
        LiveNotificationManager notificationManager = app().createNotificationManager(this);
        String[] statusLables = getResources().getStringArray(R.array.live_show_notification_labels);
        statusDisplayer = new NotificationStatusDisplayer(notificationManager, statusLables);
        stateHolder().addListenerSilently(statusDisplayer);
    }

    @Override
    public void onDestroy() {
        releaseVisual();
        releaseInfrastructure();
        super.onDestroy();
    }

    private void releaseVisual() {
        stateHolder().removeListener(statusDisplayer);
    }

    private void releaseInfrastructure() {
        networkLock.release();
        stream.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (TOGGLE_ACTION.equals(action)) {
            player.togglePlayback();
        }
        if (TIMEOUT_ACTION.equals(action)) {
            scheduler.timeoutElapsed();
        }
        return START_NOT_STICKY;
    }

    private LiveShowApp app() {
        return LiveShowApp.getInstance();
    }

    private LiveShowStateHolder stateHolder() {
        return app().stateHolder();
    }

    @Override
    public void onActivated() {
        IconNote note = app().createForegroundNotification(this);
        startForeground(note.id(), note.build());
        networkLock.acquire();
    }

    @Override
    public void onDeactivated() {
        stopSelf();
    }

    public static class NotificationStatusDisplayer implements LiveShowStateListener {
        private final String[] labels;
        private final LiveNotificationManager notificationManager;

        public NotificationStatusDisplayer(LiveNotificationManager notificationManager, String[] stateLabels) {
            this.labels = stateLabels;
            this.notificationManager = notificationManager;
        }

        private String textForState(LiveShowState state) {
            return labels[state.ordinal()-1];
        }

        @Override
        public void onStateChanged(LiveShowState state, long timestamp) {
            if (LiveShowState.isIdle(state)) {
                notificationManager.hideNotifications();
            } else {
                String text = textForState(state);
                if (LiveShowState.isActive(state)) {
                    notificationManager.showForegroundNote(text);
                } else {
                    notificationManager.showBackgroundNote(text);
                }
            }
        }
    }
}
