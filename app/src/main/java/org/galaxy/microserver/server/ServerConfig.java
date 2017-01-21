package org.galaxy.microserver.server;

/**
 * Created by OoO on 2017/1/21.
 */
public class ServerConfig {

    private int port = 9999;

    private int connectionMaximum = 2;

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
