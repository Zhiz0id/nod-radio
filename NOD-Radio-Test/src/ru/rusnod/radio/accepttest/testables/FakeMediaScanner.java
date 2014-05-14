package ru.rusnod.radio.accepttest.testables;

import junit.framework.Assert;
import ru.rusnod.radio.helpers.SyncValueHolder;
import ru.rusnod.radio.podcasts.download.MediaScanner;

import java.io.File;

public class FakeMediaScanner implements MediaScanner {
    SyncValueHolder<File> scannedFile = new SyncValueHolder<File>();

    @Override
    public void scanAudioFile(File path) {
        scannedFile.setValue(path);
    }

    public void assertScannedFile(File path) {
        Assert.assertEquals("Scanned file", path, scannedFile.getValue());
    }

    public void assertNoInteractions() {
        Assert.assertFalse("Unexpected file scan", scannedFile.await());
    }
}
