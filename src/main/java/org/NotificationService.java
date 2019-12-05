package org;

import java.util.List;

public interface NotificationService {
    void send(List<ServiceStatus> downServices);
}
