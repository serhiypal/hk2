package org.serge.ws.rs.hk2;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.runlevel.RunLevelController;
import org.glassfish.hk2.runlevel.RunLevelServiceUtilities;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.level.FastPaceService;
import org.serge.ws.rs.hk2.level.MediumPaceService;
import org.serge.ws.rs.hk2.level.PaceService;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RunLevelTest {

    @Test
    public void listener() {
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("listener");
        ServiceLocatorUtilities.addClasses(serviceLocator, PaceService.class);

        RunLevelServiceUtilities.enableRunLevelService(serviceLocator);
        RunLevelController controller = serviceLocator.getService(RunLevelController.class);
        controller.proceedTo(1);

        PaceService service = serviceLocator.getService(PaceService.class);
        service.start();
        sleepSeconds(3); // approx 3

        controller.proceedTo(3); // 1st to level 2 and then 3
        sleepSeconds(2); // approx 2
        service.stop(); // max 1

        System.out.println("Actual count is: " + service.getCount());
        assertThat("Actual is " + service.getCount(), service.getCount() >= 10, is(true));
        ServiceLocatorFactory.getInstance().destroy(serviceLocator);
    }

    @Test
    public void runLevel() {
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("runlevel");
        RunLevelServiceUtilities.enableRunLevelService(serviceLocator);

        ServiceLocatorUtilities.bind(serviceLocator,
                                     b -> b.bind(BuilderHelper.link(LongAdder.class).in(Singleton.class).build()));
        ServiceLocatorUtilities.addClasses(serviceLocator, MediumPaceService.class, FastPaceService.class);

        RunLevelController controller = serviceLocator.getService(RunLevelController.class);

        LongAdder count = serviceLocator.getService(LongAdder.class);
        // Medium is at 5-th level
        controller.proceedTo(1);
        sleepSeconds(1);
        assertThat(count.intValue(), is(0)); // Nothing has worked yet

        // Medium is at 5-th level
        controller.proceedTo(5);
        sleepSeconds(1);
        assertThat("" + count.intValue(), count.intValue() >= 1, is(true)); // Appox 1

        controller.proceedTo(10);
        sleepSeconds(5);
        assertThat("" + count.intValue(), count.intValue() >= 50, is(true));

        controller.proceedTo(3);
        int lastCount = count.intValue();
        sleepSeconds(3);
        assertThat("Last count: " + lastCount, count.intValue(), is(lastCount));

        ServiceLocatorFactory.getInstance().destroy(serviceLocator);
    }

    private static void sleepSeconds(long ms) {
        try {
            Thread.sleep(ms * 1000);
        } catch (InterruptedException e) {
            // skip
        }
    }

}
