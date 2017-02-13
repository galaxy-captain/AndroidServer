package org.galaxy.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by OoO on 2017/1/21.
 * <p>
 * 服务器运行时状态
 */
public class ServerState {

    /**
     * 服务器
     */
    private MicroServer mServer;

    /**
     * 运行状态
     */
    private boolean isRunning = false;

    /**
     * 总连接数
     */
    private int connectionTotal = 0;

    /**
     * 连接集合
     */
    private ConcurrentHashMap<String, MicroConnection> connectionMap = new ConcurrentHashMap<>();

    public ServerState(MicroServer server) {
        this.mServer = server;
    }

    public String getLocalAddress() {
        return mServer.getConfig().getLocalAddress();
    }

    public int getPort() {
        return mServer.getConfig().getPort();
    }

    /**
     * 运行状态
     */
    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    /**
     * 连接数量
     */
    public int getConnectionTotal() {
        return connectionTotal;
    }

    public int cutConnection(MicroConnection connection) {

        connectionMap.remove(connection.getName());

        return --connectionTotal;
    }

    public int addConnection(MicroConnection connection) {

        connectionMap.put(connection.getName(), connection);

        return ++connectionTotal;
    }

    /**
     * 获取一个连接
     */
    public MicroConnection getConnection(String name) {
        return connectionMap.get(name);
    }

    /**
     * 获取全部连接
     */
    public Map<String, MicroConnection> getAllConnection() {
        return connectionMap;
    }

    /**
     * 获取全部连接的名字
     */
    public List<String> getAllConnectionName() {

        List<String> list = new ArrayList<>();

        Enumeration<String> keys = connectionMap.keys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            list.add(key);
        }
        return list;
    }

    /**
     * 关闭一个连接
     */
    public void closeConnection(MicroConnection connection) {
        connection.close();
    }

    /**
     * 关闭全部连接
     */
    public void closeAllConnection() {

        Set<Map.Entry<String, MicroConnection>> set = connectionMap.entrySet();

        for (Map.Entry<String, MicroConnection> entry : set) {
            closeConnection(entry.getValue());
        }

    }

}

