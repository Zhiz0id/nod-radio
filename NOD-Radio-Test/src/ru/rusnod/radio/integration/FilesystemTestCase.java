package ru.rusnod.radio.integration;

import android.test.InstrumentationTestCase;
import ru.rusnod.radio.integration.helpers.FileUtils;
import ru.rusnod.radio.integration.helpers.FileUtils;

import java.io.File;

public class FilesystemTestCase extends InstrumentationTestCase {
    protected File workDir;

    public File cacheDir() {
        return getInstrumentation().getTargetContext().getCacheDir();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        workDir = new File(cacheDir(), getName());
        FileUtils.deleteDir(workDir);
    }

    @Override
    public void tearDown() throws Exception {
        FileUtils.deleteDir(workDir);
        super.tearDown();
    }
}
