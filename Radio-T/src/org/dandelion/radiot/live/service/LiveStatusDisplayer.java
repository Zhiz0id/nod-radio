package org.dandelion.radiot.live.service;

import org.dandelion.radiot.live.core.LiveShowState;

public interface LiveStatusDisplayer {
    void showStatus(LiveShowState state);
}
