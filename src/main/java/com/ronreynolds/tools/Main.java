package com.ronreynolds.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
            for (int i = 0; i < 10; i++) {
                executor.execute(() -> System.out.println("Hello, World!"));
            }
        }
    }
}
