package org.galaxy.androidserver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.galaxy.server.ServerConfig;
import org.galaxy.server.ServerState;
import org.galaxy.server.listener.ConnectionListener;

/**
 * Created by OoO on 2017/1/21.
 * <p>
 * SocketServer Service
 */
public class ServerService extends Service {

    public class ServerBinder extends Binder {

        ServerService getService() {
            return ServerService.this;
        }

    }

    private final ServerBinder mBinder = new ServerBinder();

    private MyServer mServer = new MyServer(this);

    private boolean hasInit = false;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (hasInit) closeServer();
    }

    /**
     * 初始化服务器
     */
    public boolean initServer() {

        hasInit = mServer.init();

        return hasInit;
    }

    /**
     * 启动服务器
     */
    public void startServer() {
        mServer.start();
    }

    /**
     * 停止服务器
     */
    public void closeServer() {
        mServer.close();
    }

    /**
     * 获取服务器配置
     */
    public ServerConfig getServerConfig(){
        return mServer.getConfig();
    }

    /**
     * 获取服务器状态信息
     */
    public ServerState getServerState() {
        return mServer.getState();
    }

    public void setConnectionListener(ConnectionListener listener) {
        mServer.setConnectionListener(listener);
    }

    public void removeConnectionListener(ConnectionListener listener) {
        mServer.removeConnectionListener(listener);
    }

}
