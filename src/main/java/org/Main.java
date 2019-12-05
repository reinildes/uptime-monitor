package org;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    static int CHECK_INTERVAL = 5;   // Check every 5 seconds

    // Executor to run the check and run reports on a service
    static ScheduledExecutorService taskScheduler = Executors.newScheduledThreadPool(1);

    private HttpMonitor httpMonitor;
    private NotificationService notificationService;

    public Main() {}

    public Main(HttpMonitor httpMonitor, NotificationService notificationService){
        this.httpMonitor = httpMonitor;
        this.notificationService = notificationService;
    }

    public static void main(String[] args) {

        var main = new Main(new HttpMonitor(), new SimpleNotificationService());

        taskScheduler.scheduleAtFixedRate(() -> {
            // Clear previously displayed report
            System.out.print("\033[H\033[2J");
            System.out.flush();

            main.runMonitor();

        },0, CHECK_INTERVAL, TimeUnit.SECONDS);
    }

    void runMonitor() {

        var serviceStatuses = getServiceStatuses();

        var statusSummary = getServicesHealthSummary(serviceStatuses);

        System.out.println(statusSummary);

        var downServices = getDownServices(serviceStatuses);

        if(!downServices.isEmpty())
            notificationService.send(downServices);
    }

    List<ServiceStatus> getDownServices(final List<ServiceStatus> serviceStatuses) {
        return serviceStatuses.stream()
                    .filter(Predicate.not(ServiceStatus::isLive))
                    .collect(Collectors.toList());
    }

    String getServicesHealthSummary(final List<ServiceStatus> serviceStatuses) {
        return serviceStatuses.stream()
                    .map(ServiceStatus::toString)
                    .reduce(new String(), (accumulator, newStatus) -> accumulator.concat(newStatus));
    }

    List<ServiceStatus> getServiceStatuses() {
        return Stream.of(Constants.DOMAINS)
        .parallel()
        .map(domain -> httpMonitor.check(domain))
        .collect(Collectors.toList());
    }
}