package org;

import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    static int CHECK_INTERVAL = 5;   // Check every 5 seconds
    static int NUM_THREADS = 3;      // Number of threads

    // Thread pool to process domains
    static ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);

    // Executor to run the check and run reports on a service
    static ScheduledExecutorService taskScheduler = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {

        taskScheduler.scheduleAtFixedRate(() -> {
            // Clear previously displayed report
            System.out.print("\033[H\033[2J");
            System.out.flush();

            runMonitor();

        },0, CHECK_INTERVAL, TimeUnit.SECONDS);
    }

    private static void runMonitor() {
        var serviceStatuses =
                Stream.of(Constants.DOMAINS)
                .parallel()
                .map(domain -> new HttpMonitor().check(domain))
                .collect(Collectors.toList());

        var statusSummary = serviceStatuses.stream()
                .map(ServiceStatus::toString)
                .reduce(new String(), (accumulator, newStatus) -> accumulator.concat(newStatus));

        System.out.println(statusSummary);

    }

}