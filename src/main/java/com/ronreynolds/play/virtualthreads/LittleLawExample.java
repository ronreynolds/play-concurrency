package com.ronreynolds.play.virtualthreads;

import com.ronreynolds.play.util.Benchmark;
import com.ronreynolds.play.util.Threads;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

@Slf4j
public class LittleLawExample {
    public static void main(String[] args) {
        final var numTasks        = 10_000;
        final var avgResponseTime = Duration.ofMillis(500);
        Runnable  ioBoundTask     = () -> Threads.sleepSilently(avgResponseTime);
        log.info("Little's law test; {} tasks with {} latency", numTasks, avgResponseTime);
        for (int x = 0; x < 2; ++x) {
            log.info("{}", Benchmark.benchmark("virtual threads", Executors.newVirtualThreadPerTaskExecutor(), ioBoundTask,
                    numTasks));
            log.info("{}", Benchmark.benchmark("fixed pool - 100", Executors.newFixedThreadPool(100), ioBoundTask, numTasks));
            log.info("{}", Benchmark.benchmark("fixed pool - 500", Executors.newFixedThreadPool(500), ioBoundTask, numTasks));
            log.info("{}", Benchmark.benchmark("fixed pool - 1000", Executors.newFixedThreadPool(1000), ioBoundTask, numTasks));
            // FIXME - pool is too small for this number of tasks.
            log.info("{}", Benchmark.benchmark("fork-join", (ForkJoinPool) Executors.newWorkStealingPool(), ioBoundTask,
                    numTasks));
        }
        log.info("done");
    }
}
