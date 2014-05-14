package ru.rusnod.radio.live.core.states;

import org.junit.Test;
import ru.rusnod.radio.live.core.LiveShowPlayer;
import ru.rusnod.radio.live.core.LiveShowState;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class StoppingTest {
    private LiveShowPlayer player = mock(LiveShowPlayer.class);
    private LiveShowState state = LiveShowState.Stopping;

    @Test
    public void togglePlaybackDoesNothing() throws Exception {
        state.togglePlayback(player);
        verifyZeroInteractions(player);
    }
}
