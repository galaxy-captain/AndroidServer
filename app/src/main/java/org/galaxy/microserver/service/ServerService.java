package org.galaxy.microserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.galaxy.microserver.server.MicroServer;

/**
 * Created by OoO on 2017/1/21.
 */
public class ServerService extends Service {

    public class ServerBinder extends Binder {
        ServerService getService() {
            return ServerService.this;
        }
    }

    private ServerBinder mBinder = new ServerBinder();

    private MicroServer mServer;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mServer = new MicroServer();

        mServer.initServer();

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

        if (mServer != null)
            mServer.closeServer();
    }

    public void startServer() {
        mServer.startServer();
    }

    public void closeServer() {

    }

}
