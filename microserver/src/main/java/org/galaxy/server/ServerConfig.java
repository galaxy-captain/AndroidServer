package org.galaxy.server;


/**
 * Created by OoO on 2017/1/21.
 * <p>
 * 服务器配置类
 */
public class ServerConfig {

    private static final String UNKNOW_IP = "unknow";

    private static final int DEFAULT_PORT = 9999;

    private static final int DEFAULT_MAX = 1;

    /**
     * 本地地址
     */
    private String localAddress;

    /**
     * 本地端口号
     */
    private int port = DEFAULT_PORT;

    /**
     * 最大连接数
     */
    private int connectionMaximum;

    public ServerConfig() {
        this(DEFAULT_PORT);
    }

    public ServerConfig(int port) {
        this(UNKNOW_IP, port);
    }

    public ServerConfig(String localAddress, int port) {
        this(localAddress, port, DEFAULT_MAX);
    }

    public ServerConfig(String localAddress, int port, int max) {
        this.localAddress = localAddress;
        this.port = port;
        this.connectionMaximum = max;
    }

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