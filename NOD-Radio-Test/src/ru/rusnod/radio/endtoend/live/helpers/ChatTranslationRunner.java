package ru.rusnod.radio.endtoend.live.helpers;

import android.app.Instrumentation;
import com.robotium.solo.Solo;
import org.hamcrest.MatcherAssert;
import ru.rusnod.radio.live.schedule.DeterministicScheduler;
import ru.rusnod.radio.live.ui.LiveShowActivity;
import ru.rusnod.radio.endtoend.live.RobotiumMatchers;

import static ru.rusnod.radio.endtoend.live.RobotiumMatchers.showsText;
import static org.hamcrest.MatcherAssert.assertThat;

public class ChatTranslationRunner extends Solo {
    private final DeterministicScheduler scheduler;

    public ChatTranslationRunner(Instrumentation instrumentation, LiveShowActivity activity, DeterministicScheduler scheduler) {
        super(instrumentation, activity);
        this.scheduler = scheduler;
    }

    public void refreshChat() {
        scheduler.performAction();
    }

    public void showsChatMessages(String... messages) {
        for (String msg: messages) {
            MatcherAssert.assertThat(this, RobotiumMatchers.showsText(msg));
        }
    }

    public void showsErrorMessage() {
        MatcherAssert.assertThat(this, RobotiumMatchers.showsText("Трансляция чата недоступна"));
    }
}
