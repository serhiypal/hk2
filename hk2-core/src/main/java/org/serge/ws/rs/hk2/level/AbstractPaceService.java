package org.serge.ws.rs.hk2.level;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.concurrent.atomic.LongAdder;

import org.jvnet.hk2.annotations.Contract;

@Contract
public class AbstractPaceService {

    private volatile boolean stop;

    private volatile int timeout;

    @Inject
    private LongAdder count;

    private Thread t;

    protected AbstractPaceService(int timeout) {
        this.timeout = timeout;
    }

    @PostConstruct
    public void start() {
        System.out.println(getClass().getSimpleName() + ".start");
        if (t != null && t.isAlive()) {
            return;
        }
        count.reset();
        stop = false;
        t = new Thread(this::run);
        t.start();
    }

    @PreDestroy
    public synchronized void stop() {
        stop = true;
        t.interrupt();
        t = null;
        System.out.println(getClass().getSimpleName() + ".stop");
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
