package org.galaxy.microserver.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import org.galaxy.microserver.server.ServerState;
import org.galaxy.microserver.server.callback.ConnectionListener;

/**
 * Created by OoO on 2017/1/21.
 */
public class ServerServiceConnection implements ServiceConnection {

    private ServerService mService;

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        ServerService.ServerBinder serverBinder = (ServerService.ServerBinder) binder;
        mService = serverBinder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public void startServer() {
        mService.startServer();
    }

    public void stopServer() {
        mService.stopServer();
    }

    public ServerState getServerState() {
        return mService.getServerState();
    }

    public void setConnectionListener(ConnectionListener listener){
        mService.setConnectionListener(listener);
    }

    public void removeConnectionListener(ConnectionListener listener){
        mService.removeConnectionListener(listener);
    }

}
