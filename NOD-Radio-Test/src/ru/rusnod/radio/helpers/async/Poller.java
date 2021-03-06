package ru.rusnod.radio.helpers.async;

import org.hamcrest.StringDescription;

public class Poller {
    private long timeoutMillis;
    private long pollDelayMillis;

    public Poller(long timeoutMillis, long pollDelayMillis) {
        this.timeoutMillis = timeoutMillis;
        this.pollDelayMillis = pollDelayMillis;
    }

    public void check(Probe probe) throws InterruptedException {
        Timeout timeout = new Timeout(timeoutMillis);

        while (!probe.isSatisfied()) {
            if (timeout.hasTimedOut()) {
                throw new AssertionError(describeFailureOf(probe));
            }
            Thread.sleep(pollDelayMillis);

            probe.sample();
        }
    }

    protected String describeFailureOf(Probe probe) {
        StringDescription description = new StringDescription();

        description.appendText("\nTried to look for:\n    ");
        probe.describeAcceptanceCriteriaTo(description);
        description.appendText("\nbut:\n    ");
        probe.describeFailureTo(description);

        return description.toString();
    }

    public static void assertEventually(Probe probe) throws InterruptedException {
        new Poller(1000L, 100L).check(probe);
    }
}