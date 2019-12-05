package org;

import java.util.List;

public class SimpleNotificationService implements NotificationService {
    @Override
    public void send(final List<ServiceStatus> downServices) {

        var serviceDownStr = downServices.stream()
                .map(ServiceStatus::toString)
                .reduce(new String(), (accumulator, newStatus) -> accumulator.concat(newStatus));

        var stringBuilder = new StringBuilder()
                .append("The following services are down:\n")
                .append(serviceDownStr)
                .append("Please verify the root cause.\n");

        this.send(stringBuilder.toString());
    }

    public void send(final String message){
        System.out.println(message);
    }
}
