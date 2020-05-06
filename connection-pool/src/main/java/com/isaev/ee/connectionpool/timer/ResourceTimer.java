package com.isaev.ee.connectionpool.timer;

import com.isaev.ee.connectionpool.connectionpool.ConnectionPool;

import java.util.Timer;
import java.util.TimerTask;

public class ResourceTimer<T> {

    Timer timer;
    TimedResource<T> resource;
    ConnectionPool<T> pool;

    public ResourceTimer(TimedResource<T> resource, ConnectionPool<T> pool) {
        this.resource = resource;
        this.pool = pool;
        this.timer = new Timer();
        this.timer.schedule(new CloseResource(), pool.getIdleDuration());
    }

    class CloseResource extends TimerTask {

        @Override
        public void run() {
            try {
                pool.releaseResource(resource);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            timer.cancel();
        }
    }
}
