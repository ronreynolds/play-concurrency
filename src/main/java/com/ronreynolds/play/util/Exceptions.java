package com.ronreynolds.play.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Exceptions {
    public static void rethrowAsRuntime(Throwable t) {
        throw new RuntimeException(t);
    }
    public static void handleInterruptedException(InterruptedException e) {
        log.error("Thread was interrupted", e);
        Thread.currentThread().interrupt();
    }
}
