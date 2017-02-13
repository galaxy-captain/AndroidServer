package org.galaxy.androidserver.newApi;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import org.galaxy.androidserver.impl.IAndroidServer;
import org.galaxy.server.MicroServer;
import org.galaxy.server.listener.ConnectionListener;

/**
 * Created by galaxy on 2017/2/9.
 */
public class AndroidServerConnection implements ServiceConnection, IAndroidServer {

    public interface Callback {
        void onServiceConnected();
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private AndroidServerService mService;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {

        mService = ((AndroidServerService.ServerBinder) binder).getService();

        if (callback != null) callback.onServiceConnected();

        Log.e("TAG","onServiceConnected");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.e("TAG","onServiceDisconnected");
    }

    @Override
    public boolean open(int port) {
        return mService.open(port);
    }

    @Override
    public boolean send(int port, String args) {
        return mService.send(port, args);
    }

    @Override
    public void addPortListen(int port, ConnectionListener listener) {
        mService.addPortListen(port, listener);
    }

    @Override
    public void removePortListen(int port) {
        mService.removePortListen(port);
    }

    @Override
    public void close(int port) {
        mService.close(port);
    }

    @Override
    public void closeAll() {
        mService.closeAll();
    }

    @Override
    public MicroServer getServer(int port) {
        return mService.getServer(port);
    }

}
