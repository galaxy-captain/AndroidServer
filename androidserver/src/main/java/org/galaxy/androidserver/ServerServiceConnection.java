package org.galaxy.androidserver;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import org.galaxy.server.MicroConnection;
import org.galaxy.server.ServerConfig;
import org.galaxy.server.ServerState;
import org.galaxy.server.listener.ConnectionListener;


/**
 * Created by galaxy on 2017/1/21.
 * <p>
 * SocketServer's connection
 */
public class ServerServiceConnection implements ServiceConnection {

    private ServerService mService;

    private ConnectionListener mListener;

    private boolean hasInit = false;

    private final ConnectionListener mServerListener = new ConnectionListener() {

        @Override
        public void onAccept(MicroConnection microConnection) {
            if (mListener != null) mListener.onAccept(microConnection);
        }

        @Override
        public void onClose(MicroConnection microConnection) {
            if (mListener != null) mListener.onClose(microConnection);
        }

        @Override
        public void onReceive(MicroConnection microConnection, byte[] bytes, int length) {
            if (mListener != null) mListener.onReceive(microConnection, bytes, length);
        }

        @Override
        public void onSend(MicroConnection microConnection, byte[] bytes) {
            if (mListener != null) mListener.onSend(microConnection, bytes);
        }
    };

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {

        ServerService.ServerBinder serverBinder = (ServerService.ServerBinder) binder;

        mService = serverBinder.getService();

        hasInit = mService.initServer();

        if (hasInit) {
            mService.startServer();
            mService.setConnectionListener(mServerListener);
        }

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    /**
     * 获取服务器配置
     */
    public ServerConfig getServerConfig() {
        return mService.getServerConfig();
    }

    /**
     * 获取服务器状态
     */
    public ServerState getServerState() {
        return mService.getServerState();
    }

    /**
     * 设置连接回调监听
     */
    public void setConnectionListener(ConnectionListener listener) {
        this.mListener = listener;
    }

    /**
     * 移除连接回调监听
     */
    public void removeConnectionListener(ConnectionListener listener) {
        mListener = null;
        mService.removeConnectionListener(mServerListener);
    }

}
