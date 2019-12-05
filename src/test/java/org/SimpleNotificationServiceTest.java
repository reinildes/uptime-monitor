package org;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SimpleNotificationServiceTest {

    @Test
    public void send() {
        var simpleNotificationService = Mockito.spy(new SimpleNotificationService());

        var serviceStatuses = Arrays.asList(
                new ServiceStatus("google.com", "1.0.0.0.1", false, false),
                new ServiceStatus("amazon.com", "1.0.0.0.1", true, false));

        simpleNotificationService.send(serviceStatuses);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(simpleNotificationService).send(captor.capture());

        var expectedMessage = "The following services are down:\n" +
                "          google.com         1.0.0.0.1		HTTP: DOWN! | HTTPS: DOWN!\n" +
                "          amazon.com         1.0.0.0.1		HTTP: UP | HTTPS: DOWN!\n" +
                "Please verify the root cause.";

        assertThat(captor.getValue(), is(expectedMessage));

    }
}