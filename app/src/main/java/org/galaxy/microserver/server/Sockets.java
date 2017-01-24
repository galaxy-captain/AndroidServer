package org.galaxy.microserver.server;

import android.content.Context;

import org.galaxy.microserver.server.callback.ConnectionListener;
import org.galaxy.microserver.utils.NetworkUtils;

/**
 * Created by galaxy on 2017/1/23.
 */
public class Sockets {

    private static final ServerConfig DEFAULT = new ServerConfig();

    private final MicroServer mServer;

    public Sockets(Context context) {
        this(context, DEFAULT);
    }

    public Sockets(Context context, ServerConfig config) {

        mServer = new MicroServer(config);

        mServer.getConfig().setLocalAddress(NetworkUtils.localAddress(context));
    }

    public void setConnectionListener(ConnectionListener listener) {
        mServer.setConnectionListener(listener);
    }

    public void removeConnectionListener(ConnectionListener listener){
        mServer.removeConnectionListener(listener);
    }

    public final boolean init() {
        return mServer.initServer();
    }

    public final void start() {
        mServer.startServer();
    }

    public final void stop() {
        mServer.closeServer();
    }

    /**
     * 获取服务器状态
     */
    public ServerState getState() {
        return mServer.getState();
    }

    /**
     * 获取服务器配置
     */
    public ServerConfig getConfig() {
        return mServer.getConfig();
    }

}
