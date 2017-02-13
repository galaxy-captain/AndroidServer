package org.galaxy.androidserver.newApi;

import android.content.Context;
import android.content.Intent;

import org.galaxy.androidserver.ServerService;
import org.galaxy.androidserver.impl.IAndroidServer;
import org.galaxy.server.MicroServer;
import org.galaxy.server.listener.ConnectionListener;

import java.util.WeakHashMap;

/**
 * Created by galaxy on 2017/2/9.
 */
public class AndroidServer implements IAndroidServer {

    public interface Callback {
        void onServiceConnected();
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private static AndroidServer mInstance = new AndroidServer();

    public static AndroidServer getInstance() {
        return mInstance;
    }

    private final WeakHashMap<Context, AndroidServerConnection> mConnections = new WeakHashMap<>();

    private volatile AndroidServerConnection mCurrentConnection;

    private boolean isStart = false;

    private AndroidServer() {

    }

    public synchronized void start(Context context) {

        if (isStart) return;

        Intent intent = new Intent(context, AndroidServerService.class);
        context.startService(intent);

        isStart = true;
    }

    public synchronized void bind(final Context context, final Callback callback) {

        AndroidServerConnection connection = new AndroidServerConnection();
        mConnections.put(context, connection);

        Intent intent = new Intent(context, AndroidServerService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);

        mCurrentConnection = connection;

        mCurrentConnection.setCallback(new AndroidServerConnection.Callback() {
            @Override
            public void onServiceConnected() {
                callback.onServiceConnected();
            }
        });

    }

    public synchronized void unbind(Context context) {

        AndroidServerConnection connection = mConnections.get(context);

        context.unbindService(connection);
    }

    @Override
    public boolean open(int port) {
        return mCurrentConnection.open(port);
    }

    @Override
    public boolean send(int port, String args) {
        return mCurrentConnection.send(port, args);
    }

    @Override
    public void addPortListen(int port, ConnectionListener listener) {
        mCurrentConnection.addPortListen(port, listener);
    }

    @Override
    public void removePortListen(int port) {
        mCurrentConnection.removePortListen(port);
    }

    @Override
    public void close(int port) {
        mCurrentConnection.close(port);
    }

    @Override
    public void closeAll() {
        mCurrentConnection.closeAll();
    }

    @Override
    public MicroServer getServer(int port) {
        return mCurrentConnection.getServer(port);
    }

}
