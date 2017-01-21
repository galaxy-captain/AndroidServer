package org.galaxy.microserver.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by OoO on 2017/1/21.
 */
public class ServerServiceConnection implements ServiceConnection {

    private ServerService mService;

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        ServerService.ServerBinder serverBinder = (ServerService.ServerBinder) binder;
        mService  = serverBinder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public void startServer(){
        mService.startServer();
    }

    public void closeServer(){
        mService.closeServer();
    }

}
