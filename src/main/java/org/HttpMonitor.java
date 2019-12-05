package org;

import java.io.*;
import java.net.*;
import java.util.*;

class HttpMonitor {

    boolean isReachable(String host, int port) {
        // Ping the provided IP and port
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 1000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public ServiceStatus check(String domain) {
        String ip = null;

        // Get the domain's IP so we can ping it
        try {
            ip = InetAddress.getByName(domain).getHostAddress();
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Invalid domain provided");
        }

        // Are HTTP and HTTPS reachable?
        boolean http = isReachable(domain, 80);
        boolean https = isReachable(domain, 443);

        // Log result to console
        return new ServiceStatus(domain, ip, http, https);
    }
}