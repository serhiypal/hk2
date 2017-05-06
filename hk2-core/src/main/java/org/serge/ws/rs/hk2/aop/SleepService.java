package org.serge.ws.rs.hk2.aop;

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
