package com.ronreynolds.play.util;

import java.time.Duration;

public class Threads {
    public static void sleepSilently(Duration time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Exceptions.handleInterruptedException(e);
        }
    }
}
