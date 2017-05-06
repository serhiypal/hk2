package org.serge.ws.rs.hk2.aop2;

import org.glassfish.hk2.extras.interception.Intercepted;

@Intercepted
public class SleepService {

    @Timed
    public SleepService() {
        sleep(500);
    }

    @Timed
    public void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            System.out.println("Sleep interrupted.");
        }
     }
}
