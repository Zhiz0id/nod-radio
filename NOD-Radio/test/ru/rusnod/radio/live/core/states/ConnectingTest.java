package ru.rusnod.radio.live.core.states;

import org.junit.Test;
import ru.rusnod.radio.live.core.LiveShowPlayer;
import ru.rusnod.radio.live.core.LiveShowState;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConnectingTest {
    private LiveShowPlayer player = mock(LiveShowPlayer.class);
    private LiveShowState state = LiveShowState.Connecting;

    @Test
    public void togglePlaybackGoesStopping() throws Exception {
        state.togglePlayback(player);
        verify(player).beStopping();
    }

    @Test
    public void onErrorGoesWaiting() throws Exception {
        state.handleError(player);
        verify(player).beWaiting();
    }
}
