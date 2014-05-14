package ru.rusnod.radio.live.core.states;

import org.junit.Test;
import ru.rusnod.radio.live.core.LiveShowPlayer;
import ru.rusnod.radio.live.core.LiveShowState;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class IdleTest {
    private LiveShowPlayer player = mock(LiveShowPlayer.class);
    private LiveShowState state = LiveShowState.Idle;

    @Test
    public void togglePlaybackInitiatesConnection() throws Exception {
        state.togglePlayback(player);
        verify(player).beConnecting();
    }
}
