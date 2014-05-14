package ru.rusnod.radio.live.core.states;

import org.junit.Test;
import ru.rusnod.radio.live.core.LiveShowPlayer;
import ru.rusnod.radio.live.core.LiveShowState;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WaitingTest {
    private final LiveShowPlayer player = mock(LiveShowPlayer.class);
    private final LiveShowState state = LiveShowState.Waiting;

    @Test
    public void togglePlaybackGoesIdle() throws Exception {
        state.togglePlayback(player);
        verify(player).beIdle();
    }
}
