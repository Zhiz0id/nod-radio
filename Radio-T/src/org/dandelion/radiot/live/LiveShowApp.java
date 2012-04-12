package org.dandelion.radiot.live;

import org.dandelion.radiot.R;
import android.content.Context;
import org.dandelion.radiot.live.core.AudioStream;
import org.dandelion.radiot.live.core.LiveShowStateHolder;
import org.dandelion.radiot.live.service.LiveShowClient;
import org.dandelion.radiot.live.ui.LiveShowActivity;
import org.dandelion.radiot.live.ui.LiveStatusDisplayer;
import org.dandelion.radiot.live.ui.NotificationStatusDisplayer;
import org.dandelion.radiot.util.IconNote;

public class LiveShowApp {
    private static LiveShowApp instance = new LiveShowApp();
    //private static final String LIVE_SHOW_URL = "http://radio10.promodeejay.net:8181/stream";
    private static final String LIVE_SHOW_URL = "http://icecast.bigrradio.com/80s90s";
    private LiveShowStateHolder stateHolder = LiveShowStateHolder.initial();
    private static final int LIVE_NOTE_ID = 1;
    private static final int FOREGROUND_NOTE_ID = 2;

    public static LiveShowApp getInstance() {
        return instance;
    }

    public static void setTestingInstance(LiveShowApp app) {
        instance = app;
    }

    public LiveShowApp() {
    }

    public AudioStream createAudioStream() {
        return new MediaPlayerStream(LIVE_SHOW_URL);
    }
    
    public LiveShowClient createClient(Context context) {
        return new LiveShowClient(context, stateHolder);
    }

    public LiveShowStateHolder stateHolder() {
        return stateHolder;
    }

    public LiveStatusDisplayer createStatusDisplayer(Context context) {
        String[] labels = context.getResources().getStringArray(R.array.live_show_notification_labels);
        IconNote note = createNote(context);
        return new NotificationStatusDisplayer(note, labels);
    }

    public IconNote createNote(final Context context) {
        return new IconNote(context, LIVE_NOTE_ID) {{
            setTitle(context.getString(R.string.app_name));
            setIcon(R.drawable.stat_live);
            showsActivity(LiveShowActivity.class);
            beOngoing();
        }};
    }

    public IconNote createForegroundNote(final Context context) {
        // Creating the invisible note simply to satisfy
        // the Android Service.startForeground() requirements
        return new IconNote(context, FOREGROUND_NOTE_ID);
    }
}
