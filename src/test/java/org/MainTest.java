package org;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MainTest {

    NotificationService notificationService = mock(SimpleNotificationService.class);
    HttpMonitor httpMonitor = mock(HttpMonitor.class);

    private final Main main = spy(new Main(httpMonitor, notificationService));

    @Test
    public void runMonitorShouldSendNotificationForDownStatuses() {
        doReturn(List.of()).when(main).getServiceStatuses();
        doReturn(Collections.singletonList(new ServiceStatus("ahost.com", "10.0.0.1", true, false)))
                .when(main).getDownServices(anyList());

        main.runMonitor();

        verify(notificationService).send(anyList());
    }

    @Test
    public void runMonitorShouldNotSendNotificationWhenDownStatusesReturnsEmpty() {
        doReturn(List.of()).when(main).getServiceStatuses();
        doReturn(List.of())
                .when(main).getDownServices(anyList());

        main.runMonitor();

        verify(notificationService, never()).send(anyList());
    }

    @Test

    public void getDownServicesReturnDownServices() {
        var listWithDownService = Collections.singletonList(new ServiceStatus("ahost.com", "10.0.0.1", true, false));

        var downServices = main.getDownServices(listWithDownService);

        assertThat(downServices.size(), is(1));
    }

    @Test
    public void getDownServicesDoesNotReturnUpServices() {
        var listWithDownService = Collections.singletonList(new ServiceStatus("ahost.com", "10.0.0.1", true, true));

        var downServices = main.getDownServices(listWithDownService);

        assertThat(downServices.size(), is(0));
    }

    @Test
    public void getServicesHealthSummary() {
        Main main = spy(new Main());

        var services = List.of(
                new ServiceStatus("google.com", "1.0.0.1", true, true),
                new ServiceStatus("amazon.com", "1.0.0.1", false, true),
                new ServiceStatus("yahoo.com", "1.0.0.1", true, false));

        var expectedMessage =
                "          google.com           1.0.0.1		HTTP: UP | HTTPS: UP\n" +
                "          amazon.com           1.0.0.1		HTTP: DOWN! | HTTPS: UP\n" +
                "           yahoo.com           1.0.0.1		HTTP: UP | HTTPS: DOWN!\n" ;

        var servicesHealthSummary = main.getServicesHealthSummary(services);

        assertThat(servicesHealthSummary, is(expectedMessage));
    }

  }