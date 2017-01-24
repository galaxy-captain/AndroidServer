package org.galaxy.microserver.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by galaxy on 2017/1/23.
 *
 */
final class HeartbeatThread extends Thread {

    private MicroServer mServer;

    private List<MicroConnection> connections = new ArrayList<>();

    public HeartbeatThread(MicroServer server) {
        this.mServer = server;
    }

    public void addConnection(MicroConnection connection) {
        connections.add(connection);
    }

    public synchronized void removeConnection(MicroConnection connection) {
        connections.remove(connection);
    }

    private void execute() {

        int size = connections.size();

        for (int i = 0; i < size; i++) {

            MicroConnection connection = connections.get(i);

            boolean isConnect = connection.testConnection();

            if (!isConnect) removeConnection(connection);

        }

    }

    @Override
    public void run() {

        while (mServer != null && mServer.getState().isRunning()) {

            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            execute();
        }

    }

}
