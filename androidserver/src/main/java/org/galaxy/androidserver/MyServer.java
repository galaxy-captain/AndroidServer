package org.galaxy.androidserver;

import android.content.Context;

import org.galaxy.androidserver.utils.NetworkUtils;
import org.galaxy.server.MicroServer;
import org.galaxy.server.ServerConfig;
import org.galaxy.server.ServerState;
import org.galaxy.server.listener.ConnectionListener;

/**
 * 服务器
 */
public class MyServer {

    private static final ServerConfig DEFAULT = new ServerConfig();

    private Context mContext;

    private final MicroServer mServer;

    public MyServer(Context context) {
        this(context, DEFAULT);
    }

    public MyServer(Context context, ServerConfig config) {

        this.mContext = context;

        mServer = new MicroServer(config);
    }

    public final boolean init() {

        // 设置本地地址
        mServer.getConfig().setLocalAddress(NetworkUtils.localAddress(mContext));

        return mServer.initServer();
    }

    public final void start() {
        mServer.startServer();
    }

    public final void close() {
        mServer.closeServer();
    }

    public void setConnectionListener(ConnectionListener listener) {
        mServer.setConnectionListener(listener);
    }

    public void removeConnectionListener(ConnectionListener listener) {
        mServer.removeConnectionListener();
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
