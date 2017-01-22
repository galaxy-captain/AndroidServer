package org.galaxy.microserver.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by OoO on 2017/1/21.
 */
public class ServerState {

    private MicroServer mServer;

    private boolean isRunning = false;

    private int connectionTotal = 0;

    private Map<String, MicroConnection> connectionMap = new HashMap<>();

    public ServerState(MicroServer server) {
        this.mServer = server;
    }

    public String getLocalAddress() {
        return mServer.getConfig().getLocalAddress();
    }

    public int getPort() {
        return mServer.getConfig().getPort();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

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

    public MicroConnection getConnection(String name) {
        return connectionMap.get(name);
    }

    public Map<String, MicroConnection> getAllConnection() {
        return connectionMap;
    }

    public List<String> getAllConnectionName() {
        return new ArrayList<>(connectionMap.keySet());
    }


}
