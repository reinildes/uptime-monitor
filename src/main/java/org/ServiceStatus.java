package org;

public class ServiceStatus {
    public String name;
    public String ip;
    public boolean http;
    public boolean https;

    public ServiceStatus(String name, String ip, boolean http, boolean https) {
        this.name = name;
        this.ip = ip;
        this.http = http;
        this.https = https;
    }

    public boolean isLive(){
        return http && https;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append(String.format("%20s", name))
            .append(String.format("%18s", ip))
            .append("\t\tHTTP: ")
            .append(http ? "UP" : "DOWN!")
            .append(" | HTTPS: ")
            .append(https ? "UP" : "DOWN!")
            .append("\n")
            .toString();
    }
}