package ru.rusnod.radio.integration;

import android.app.ActivityManager;
import android.content.Context;
import android.test.InstrumentationTestCase;
import ru.rusnod.radio.live.LiveShowApp;
import ru.rusnod.radio.live.core.AudioStream;
import ru.rusnod.radio.live.LiveShowClient;
import ru.rusnod.radio.live.service.LiveShowService;
import ru.rusnod.radio.live.service.Lockable;

import java.io.IOException;
import java.util.List;

public class LiveShowServiceLifecycleTest extends InstrumentationTestCase {
    private FakeAudioStream audioStream = new FakeAudioStream();
    private FakeLock lock = new FakeLock();

    public void testStartStopServiceInStraightLifecycle() throws Exception {
        startPlayback();
        assertServiceIsStarted();
        stopPlayback();
        assertServiceIsStopped();
    }

    public void testManagesWifiLockInStandardLifecycle() throws Exception {
        startPlayback();
        assertTrue(lock.isAcquired);
        stopPlayback();
        assertFalse(lock.isAcquired);
    }

    public void testReleasesWifiLockInWaitingLifecycle() throws Exception {
        startPlayback();
        audioStream.signalError();
        assertFalse(lock.isAcquired);
        stopPlayback();
        assertFalse(lock.isAcquired);
    }

    public void testStopServiceWhenPlayerGoesWaiting() throws Exception {
        startPlayback();
        assertServiceIsStarted();

        audioStream.signalError();
        assertServiceIsStopped();

        stopPlayback();
        assertServiceIsStopped();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        LiveShowApp.setTestingInstance(createTestingApp());
    }

    private LiveShowApp createTestingApp() {
        return new LiveShowApp() {
            @Override
            public AudioStream createAudioStream() {
                return audioStream;
            }

            @Override
            public Lockable createNetworkLock(Context context) {
                return lock;
            }
        };
    }

    private void assertServiceIsStarted() {
        ActivityManager.RunningServiceInfo info = liveServiceInfo();
        assertNotNull("Service is not running", info);
        assertTrue("Service is not foreground", info.foreground);
    }

    private void assertServiceIsStopped() {
        assertNull("Service is still running", liveServiceInfo());
    }

    private void stopPlayback() throws InterruptedException {
        togglePlayback();
    }

    private void startPlayback() throws InterruptedException {
        togglePlayback();
    }

    private void togglePlayback() throws InterruptedException {
        LiveShowClient client = LiveShowApp.getInstance().createClient(context());
        client.togglePlayback();
        Thread.sleep(500);  // Give a service some time to process the request
    }

    private Context context() {
        return getInstrumentation().getTargetContext();
    }

    private ActivityManager.RunningServiceInfo liveServiceInfo() {
        ActivityManager manager = (ActivityManager) context().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = manager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : infos) {
            if (info.service.getClassName().equals(LiveShowService.class.getName())) {
                return info;
            }
        }
        return null;
    }

    private static class FakeLock implements Lockable {
        public boolean isAcquired;

        @Override
        public void release() {
            isAcquired = false;
        }

        @Override
        public void acquire() {
            isAcquired = true;
        }
    }

    private class FakeAudioStream implements AudioStream {
        private Listener listener;

        @Override
        public void setStateListener(Listener listener) {
            this.listener = listener;
        }

        @Override
        public void play() throws IOException {
            listener.onStarted();
        }

        @Override
        public void stop() {
            listener.onStopped();
        }

        @Override
        public void release() {
        }

        public void signalError() {
            listener.onError();
        }
    }
}
