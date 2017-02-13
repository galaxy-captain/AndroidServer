package org.galaxy.server.manager;

import org.galaxy.server.MicroConnection;
import org.galaxy.server.MicroServer;
import org.galaxy.server.ServerConfig;
import org.galaxy.server.listener.ConnectionListener;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by galaxy on 2017/2/9.
 */
public class ServerManager implements IServerManager {

    public final Map<String, MicroServer> mServerMap = new Hashtable<>();

    private String mAddress = "unknow";

    public ServerManager(String localAddress) {
        this.mAddress = localAddress;
    }

    @Override
    public boolean open(int port) {

        ServerConfig config = new ServerConfig(mAddress, port);

        MicroServer newServer = new MicroServer(config);

        boolean init = newServer.initServer();

        if (!init) return false;

        newServer.startServer();

        mServerMap.put(autoKey(port), newServer);

        return true;
    }

    @Override
    public boolean send(int port, String args) {

        MicroServer server = mServerMap.get(autoKey(port));

        List<String> connections = server.getAllConnectionName();

        if (connections == null || connections.size() == 0) return false;

        MicroConnection connection = server.getConnection(connections.get(0));

        return connection != null && connection.sendMessageAtMain(args);
    }

    @Override
    public void addPortListen(int port, ConnectionListener listener) {
        MicroServer server = mServerMap.get(autoKey(port));
        server.setConnectionListener(listener);
    }

    @Override
    public void removePortListen(int port) {
        MicroServer server = mServerMap.get(autoKey(port));
        server.removeConnectionListener();
    }

    @Override
    public void close(int port) {
        close(autoKey(port));
    }

    public void close(String name) {
        MicroServer oldServer = mServerMap.remove(name);
        oldServer.closeServer();
    }

    @Override
    public void closeAll() {

        Set<String> keys = mServerMap.keySet();

        for (String key : keys)
            close(key);

    }

    @Override
    public MicroServer getServer(int port) {
        return mServerMap.get(autoKey(port));
    }

    private String autoKey(int port) {
        return mAddress + ":" + port;
    }

}
