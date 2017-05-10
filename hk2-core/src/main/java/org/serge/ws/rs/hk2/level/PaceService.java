package org.serge.ws.rs.hk2.level;

import javax.inject.Singleton;
import java.util.concurrent.atomic.LongAdder;

import org.glassfish.hk2.runlevel.ChangeableRunLevelFuture;
import org.glassfish.hk2.runlevel.ErrorInformation;
import org.glassfish.hk2.runlevel.RunLevelFuture;
import org.glassfish.hk2.runlevel.RunLevelListener;

@Singleton
public class PaceService implements RunLevelListener {

    private static final int DEFAULT_TIMEOUT = 1000;

    private volatile boolean stop;

    private volatile int timeout;

    private final LongAdder count = new LongAdder();

    private Thread t;

    @Override
    public void onProgress(ChangeableRunLevelFuture changeableRunLevelFuture, int i) {
        synchronized (this) {
            timeout = i <= 0 ? DEFAULT_TIMEOUT : DEFAULT_TIMEOUT / i;
            System.out.println(String.format("Count: %d, gained level: %d, timeout is: %dms", count.intValue(), i, timeout));
        }
    }

    @Override
    public void onCancelled(RunLevelFuture runLevelFuture, int i) {
    }

    @Override
    public void onError(RunLevelFuture runLevelFuture, ErrorInformation errorInformation) {
    }

    public void start() {
        if (t != null && t.isAlive()) {
            return;
        }
        timeout = DEFAULT_TIMEOUT;
        count.reset();
        stop = false;
        t = new Thread(this::run);
        t.start();
    }

    public synchronized void stop() {
        stop = true;
        t.interrupt();
        t = null;
    }

    public int getCount() {
        return count.intValue();
    }

    private void run() {
        while (!stop) {
            try {
                synchronized (this) {
                    count.increment();
                    Thread.sleep(timeout);
                }
            } catch (InterruptedException e) {
                stop = true;
            }
        }
    }
}
