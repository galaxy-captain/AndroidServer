package org.galaxy.microserver.server;

/**
 * Created by OoO on 2017/1/21.
 */
public class ServerConfig {

    private String localAddress = "x.x.x.x";

    private int port = 9999;

    private int connectionMaximum = 5;

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getConnectionMaximum() {
        return connectionMaximum;
    }

    public void setConnectionMaximum(int connectionMaximum) {
        this.connectionMaximum = connectionMaximum;
    }

}
