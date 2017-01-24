package org.galaxy.microserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.galaxy.microserver.server.MicroServer;
import org.galaxy.microserver.server.ServerState;
import org.galaxy.microserver.server.Sockets;
import org.galaxy.microserver.server.callback.ConnectionListener;
import org.galaxy.microserver.utils.L;
import org.galaxy.microserver.utils.NetworkUtils;

/**
 * Created by OoO on 2017/1/21.
 *
 * 服务器代理服务
 */
public class ServerService extends Service {

    public class ServerBinder extends Binder {
        ServerService getService() {
            return ServerService.this;
        }
    }

    private final ServerBinder mBinder = new ServerBinder();

    private  Sockets mServer = new Sockets(this);

    @Override
    public IBinder onBind(Intent intent) {

        L.error("onBind");

        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        L.error("onCreate");

        if (initServer()) startServer();

    }

    @Override
    public boolean onUnbind(Intent intent) {

        L.error("onUnbind");

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopServer();
    }

    public boolean initServer() {
        return mServer.init();
    }

    public void startServer() {
        mServer.start();
    }

    public void stopServer() {
        mServer.stop();
    }

    public ServerState getServerState() {
        return mServer.getState();
    }

    public void setConnectionListener(ConnectionListener listener){
        mServer.setConnectionListener(listener);
    }

    public void removeConnectionListener(ConnectionListener listener){
        mServer.removeConnectionListener(listener);
    }

}
