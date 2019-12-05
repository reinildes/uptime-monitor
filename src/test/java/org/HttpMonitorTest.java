package org;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HttpMonitorTest {

    public String dummyHost = "host.com";

    @Test
    public void whenBothHttpsAndHttpReachableServiceStatusIsLive() {
        HttpMonitor monitor = spy(new HttpMonitor());
        doReturn(true).when(monitor).isReachable(dummyHost, 80);
        doReturn(true).when(monitor).isReachable(dummyHost, 443);

        var serviceStatus = monitor.check(dummyHost);

        assertThat(serviceStatus.isLive(), is(true));
        assertThat(serviceStatus.toString(), containsString("HTTP: UP | HTTPS: UP"));
    }

    @Test
    public void whenHttpsOrHttpNotReachableServiceStatusIsNotLive() {
        HttpMonitor monitor = spy(new HttpMonitor());
        doReturn(false).when(monitor).isReachable(dummyHost, 80);
        doReturn(true).when(monitor).isReachable(dummyHost, 443);

        var serviceStatus = monitor.check(dummyHost);

        assertThat(serviceStatus.isLive(), is(false));
    }

    @Test
    public void whenHttpNotReachableServiceStatusToStringShowsHttpDown() {
        HttpMonitor monitor = spy(new HttpMonitor());
        doReturn(false).when(monitor).isReachable(dummyHost, 80);
        doReturn(true).when(monitor).isReachable(dummyHost, 443);

        var serviceStatus = monitor.check(dummyHost);

        assertThat(serviceStatus.isLive(), is(false));
        assertThat(serviceStatus.toString(), containsString("HTTP: DOWN! | HTTPS: UP"));
    }

    @Test
    public void whenHttpsNotReachableServiceStatusToStringShowsHttpDown() {
        HttpMonitor monitor = spy(new HttpMonitor());
        doReturn(true).when(monitor).isReachable(dummyHost, 80);
        doReturn(false).when(monitor).isReachable(dummyHost, 443);

        var serviceStatus = monitor.check(dummyHost);

        assertThat(serviceStatus.isLive(), is(false));
        assertThat(serviceStatus.toString(), containsString("HTTP: UP | HTTPS: DOWN!"));
    }
}