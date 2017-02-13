package org.galaxy.androidserver.newApi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.galaxy.androidserver.impl.IAndroidServer;
import org.galaxy.androidserver.utils.NetworkUtils;
import org.galaxy.server.MicroServer;
import org.galaxy.server.listener.ConnectionListener;
import org.galaxy.server.manager.ServerManager;

/**
 * Created by galaxy on 2017/2/9.
 */
public class AndroidServerService extends Service implements IAndroidServer {

    public class ServerBinder extends Binder {
        AndroidServerService getService() {
            return AndroidServerService.this;
        }
    }

    private ServerManager mServer;

    @Override
    public IBinder onBind(Intent intent) {

        Log.e("TAG","onBind");

        return new ServerBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("TAG","onCreate");

        mServer = new ServerManager(NetworkUtils.localAddress(getBaseContext()));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("TAG","onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("TAG","onDestroy");

        if (mServer != null) mServer.closeAll();
    }

    @Override
    public boolean open(int port) {
        return mServer.open(port);
    }

    @Override
    public boolean send(int port, String args) {
        return mServer.send(port,args);
    }

    @Override
    public void addPortListen(int port, ConnectionListener listener) {
        mServer.addPortListen(port, listener);
    }

    @Override
    public void removePortListen(int port) {
        mServer.removePortListen(port);
    }

    @Override
    public void close(int port) {
        mServer.close(port);
    }

    @Override
    public void closeAll() {
        mServer.closeAll();
    }

    @Override
    public MicroServer getServer(int port) {
        return mServer.getServer(port);
    }

}
