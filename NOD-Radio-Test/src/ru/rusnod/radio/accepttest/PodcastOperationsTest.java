package ru.rusnod.radio.accepttest;


import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import ru.rusnod.radio.accepttest.drivers.AppNavigator;
import ru.rusnod.radio.accepttest.drivers.PodcastListUiDriver;
import ru.rusnod.radio.accepttest.testables.*;
import ru.rusnod.radio.home_screen.HomeScreenActivity;
import ru.rusnod.radio.http.HttpClient;
import ru.rusnod.radio.podcasts.core.PodcastItem;
import ru.rusnod.radio.podcasts.download.FakeDownloaderActivity;
import ru.rusnod.radio.podcasts.main.PodcastClientPlatform;
import ru.rusnod.radio.podcasts.main.PodcastsApp;
import ru.rusnod.radio.podcasts.ui.PodcastListActivity;
import ru.rusnod.radio.accepttest.drivers.AppNavigator;
import ru.rusnod.radio.accepttest.testables.*;

import java.io.File;
import java.io.IOException;

public class PodcastOperationsTest extends
        ActivityInstrumentationTestCase2<HomeScreenActivity> {
    public static final String SAMPLE_URL = "http://example.com/podcast_file.mp3";
    private static final String TITLE = "Радио-Т 001";

    private FakePodcastPlayer player;
    private FakeDownloadManager downloadManager;
    private TestingPodcastsApp application;
    private AppNavigator appDriver;
    private FakeMediaScanner mediaScanner;
    private FakeNotificationManager notificationManager;

    public PodcastOperationsTest() {
        super(HomeScreenActivity.class);
    }

    @Override
	protected void setUp() throws Exception {
		super.setUp();
        setupEnvironment();
        appDriver = createDriver();
    }

    public void testPlayPodcastFromInternet() throws Exception {
        PodcastListUiDriver driver = gotoPodcastListPage();
        PodcastItem item = driver.selectItemForPlaying(0);
        player.assertIsPlaying(item.audioUri);
	}

    public void testDownloadPodcastFileLocally() throws Exception {
        PodcastListUiDriver driver = gotoPodcastListPage();
        driver.makeSamplePodcastWithUrl(TITLE, SAMPLE_URL);
        File localPath = new File(getDownloadFolder(), "podcast_file.mp3");

        driver.selectItemForDownloading(0);
        downloadManager.assertSubmittedRequest(SAMPLE_URL, localPath);
        downloadManager.downloadComplete();

        mediaScanner.assertScannedFile(localPath);
        notificationManager.assertShowsSuccess(TITLE, localPath);
    }

    public void testDownloadFinishedWithError() throws Exception {
        PodcastListUiDriver driver = gotoPodcastListPage();
        driver.makeSamplePodcastWithUrl(TITLE, SAMPLE_URL);
        File localPath = new File(getDownloadFolder(), "podcast_file.mp3");
        final int errorCode = 1000;

        driver.selectItemForDownloading(0);
        downloadManager.assertSubmittedRequest(SAMPLE_URL, localPath);
        downloadManager.downloadAborted(errorCode);

        mediaScanner.assertNoInteractions();
        notificationManager.assertShowsError(TITLE, errorCode);
    }

    public void testMissingPodcastUrl() throws Exception {
        PodcastListUiDriver driver = gotoPodcastListPage();
        driver.makeSamplePodcastWithUrl(TITLE, null);
        
        driver.selectItemForDownloading(0);
        assertTrue(driver.waitForText("Неверная ссылка на аудио-файл подкаста"));
    }

    public void testCancelDownloadInProgress() throws Exception {
        PodcastListUiDriver driver = gotoPodcastListPage();
        driver.makeSamplePodcastWithUrl(TITLE, SAMPLE_URL);
        File localPath = new File(getDownloadFolder(), "podcast_file.mp3");

        driver.selectItemForDownloading(0);
        downloadManager.assertSubmittedRequest(SAMPLE_URL, localPath);
        downloadManager.cancelDownload();
        mediaScanner.assertNoInteractions();
    }

    public void testInformsUserOnUnsupportedPlatforms() throws Exception {
        application.setDownloadSupported(false);
        PodcastListUiDriver driver = gotoPodcastListPage();

        driver.selectItemForDownloading(0);

        appDriver.assertCurrentActivity(
                "Should inform user of unsupported platform",
                FakeDownloaderActivity.class);

    }

    private PodcastListUiDriver gotoPodcastListPage() throws InterruptedException {
        appDriver.goToPodcastsScreen();
        assertTrue(appDriver.waitForActivity(PodcastListActivity.class.getSimpleName()));
        Thread.sleep(1000);
        PodcastListActivity activity = (PodcastListActivity) appDriver.getCurrentActivity();
        return new PodcastListUiDriver(getInstrumentation(), activity);
    }

    private void setupEnvironment() {
        setupOperationsPlatform();
        setupFakeLoader();
    }

    private void setupFakeLoader() {
        PodcastListActivity.clientFactory = new PodcastClientPlatform(getInstrumentation().getTargetContext()) {
            @Override
            protected HttpClient newThumbnailClient() {
                return new HttpClient() {

                    @Override
                    public String getStringContent(String url) throws IOException {
                        return null;
                    }

                    @Override
                    public byte[] getByteContent(String url) throws IOException {
                        return new byte[0];
                    }

                    @Override
                    public void shutdown() {
                    }
                };
            }
        };
    }

    private void setupOperationsPlatform() {
        player = new FakePodcastPlayer();
        downloadManager = new FakeDownloadManager(getInstrumentation().getTargetContext());
        mediaScanner = new FakeMediaScanner();
        notificationManager = new FakeNotificationManager();
        application = new TestingPodcastsApp(getInstrumentation().getContext(),
                player, downloadManager, mediaScanner, notificationManager);
        application.setDownloadFolder(getDownloadFolder());
        PodcastsApp.setTestingInstance(application);
    }

    private static File getDownloadFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    protected AppNavigator createDriver() {
        return new AppNavigator(getInstrumentation(), getActivity());
    }
}

