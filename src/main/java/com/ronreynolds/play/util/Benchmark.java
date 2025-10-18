package com.ronreynolds.play.util;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.LongAdder;

public class Benchmark {
    public record Result(String description, int startedTasks, int finishedTasks, float tasksPerSec, Duration duration) {
        static Result of(String description, int startedTasks, int finishedTasks, Duration duration) {
            return new Result(description, startedTasks, finishedTasks, (float) (finishedTasks / duration.toMillis()) * 1_000,
                    duration);
        }
    }

    /**
     * Benchmark the execution of a number of tasks using the provided thread pool.
     *
     * @param description - a description of the benchmark
     * @param threadPool  - the ExecutorService to use for executing tasks (will be closed after use)
     * @param task        - the task to be executed
     * @param numTasks    - the number of times to execute the task
     * @return a Result object containing the benchmark results
     */
    public static Result benchmark(String description, ExecutorService threadPool, Runnable task, int numTasks) {
        final long      startTime     = System.nanoTime();
        final LongAdder finishedTasks = new LongAdder();
        try (threadPool) {
            for (int i = 0; i < numTasks; ++i) {
                threadPool.execute(() -> {
                    task.run();
                    finishedTasks.increment();
                });
            }
        }
        return Result.of(description, numTasks, finishedTasks.intValue(), Duration.ofNanos(System.nanoTime() - startTime));
    }

    public static Result benchmark(String description, ForkJoinPool threadPool, Runnable task, int numTasks) {
        final long      startTime     = System.nanoTime();
        final LongAdder finishedTasks = new LongAdder();
        try (threadPool) {
            for (int i = 0; i < numTasks; ++i) {
                CompletableFuture.runAsync(task, threadPool).thenRun(finishedTasks::increment);
            }
        }
        return Result.of(description, numTasks, finishedTasks.intValue(), Duration.ofNanos(System.nanoTime() - startTime));
    }
}
