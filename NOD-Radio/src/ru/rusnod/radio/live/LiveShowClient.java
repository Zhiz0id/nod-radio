package ru.rusnod.radio.live;

import android.content.Context;
import ru.rusnod.radio.live.core.LiveShowStateHolder;
import ru.rusnod.radio.live.core.LiveShowStateListener;
import ru.rusnod.radio.live.service.LiveShowService;

public class LiveShowClient {
    private LiveShowStateHolder stateHolder;
    private Context context;

    public LiveShowClient(Context context, LiveShowStateHolder stateHolder) {
        this.context = context;
        this.stateHolder = stateHolder;
    }

    public void togglePlayback() {
        LiveShowService.sendTogglePlayback(context);
    }

    public void addListener(LiveShowStateListener listener) {
        stateHolder.addListener(listener);
    }

    public void removeListener(LiveShowStateListener listener) {
        stateHolder.removeListener(listener);
    }
}
