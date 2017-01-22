package org.galaxy.microserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.galaxy.microserver.server.MicroServer;
import org.galaxy.microserver.server.ServerState;
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

    private final MicroServer mServer = new MicroServer();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (initServer()) startServer();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        closeServer();
    }

    public boolean initServer() {

        String localAddress = NetworkUtils.localAddress(this);

        mServer.getConfig().setLocalAddress(localAddress);

        return mServer.initServer();
    }

    public void startServer() {
        mServer.startServer();
    }

    public void closeServer() {
        mServer.closeServer();
    }

    public ServerState getServerState() {
        return mServer.getState();
    }

}
