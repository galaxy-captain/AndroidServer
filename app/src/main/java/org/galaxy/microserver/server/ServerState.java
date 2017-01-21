package org.galaxy.microserver.server;

import java.util.HashMap;
import java.util.Map;

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

    public MicroConnection getConnection(String name){
        return connectionMap.get(name);
    }

}
